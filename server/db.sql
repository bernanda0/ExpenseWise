-- creating sequence for serial
CREATE SEQUENCE uid_seq START 1;
CREATE SEQUENCE wid_seq START 1;
CREATE SEQUENCE eid_seq START 1;
CREATE SEQUENCE iid_seq START 1;
CREATE SEQUENCE ecid_seq START 1;
CREATE SEQUENCE icid_seq START 1;

-- creating enum
CREATE TYPE e_wallet AS ENUM ('OVO', 'GOPAY', 'DANA');

-- creating table 
CREATE TABLE wallet (
    wid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('w', nextval('wid_seq')),
    total_income INTEGER DEFAULT 0,
    total_expense INTEGER DEFAULT 0,
    balance INTEGER DEFAULT 0,
    connected_wallet e_wallet [] DEFAULT '{}' :: e_wallet []
);

CREATE TABLE users (
    uid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('u', nextval('uid_seq')),
    username VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(30) UNIQUE NOT NULL,
    img TEXT,
    password VARCHAR(128),
    phone_number VARCHAR(16),
    occupation TEXT,
    points INTEGER DEFAULT 0,
    wid TEXT REFERENCES wallet (wid) ON DELETE CASCADE
);

CREATE TABLE expense_category (
    ecid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('ec', nextval('ecid_seq')),
    ec_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE expense (
    eid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('e', nextval('eid_seq')),
    ecid TEXT REFERENCES expense_category (ecid) ON DELETE CASCADE,
    uid TEXT REFERENCES users (uid) ON DELETE CASCADE,
    amount INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    description TEXT,
    percentage FLOAT,
    snapshot_balance INTEGER
);

CREATE TABLE expense_level (
    percentage_range_lo FLOAT NOT NULL,
    percentage_range_hi FLOAT NOT NULL,
    color_level VARCHAR(10) UNIQUE NOT NULL,
    PRIMARY KEY (percentage_range_lo, percentage_range_hi)
);

CREATE TABLE income_category (
    icid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('ic', nextval('icid_seq')),
    ic_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE income (
    iid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('i', nextval('iid_seq')),
    icid TEXT REFERENCES income_category (icid) ON DELETE CASCADE,
    uid TEXT REFERENCES users (uid) ON DELETE CASCADE,
    amount INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    description TEXT
);

CREATE TABLE user_rank (
    point_range_lo INTEGER NOT NULL,
    point_range_hi INTEGER NOT NULL,
    title VARCHAR(30) UNIQUE NOT NULL,
    PRIMARY KEY (point_range_lo, point_range_hi)
);

-- inserting base record
INSERT INTO
    expense_category (ec_name)
VALUES
    ('Others'),
    ('Food'),
    ('Transportation'),
    ('Entertainment'),
    ('Shopping'),
    ('Bills');

INSERT INTO
    income_category (ic_name)
VALUES
    ('Others'),
    ('Salary'),
    ('Gift'),
    ('Investment');

INSERT INTO
    expense_level
VALUES
    (-100, -1, 'Black'),
    (0, 10, 'Green'),
    (11, 30, 'Yellow'),
    (31, 50, 'Orange'),
    (51, 100, 'Red');

INSERT INTO
    user_rank
VALUES
    (-9999, -1, 'Waster'),
    (0, 999, 'Mediocre'),
    (1000, 2999, 'Saver'),
    (3000, 5999, 'Wise'),
    (6000, 9999, 'Lord');

-- creating function
CREATE FUNCTION update_wallet() RETURNS TRIGGER AS $$
BEGIN
    DECLARE
        total_in INTEGER;
        total_ex INTEGER;
        wallet_id TEXT;
        user_id TEXT;
    BEGIN
        IF (TG_OP = 'INSERT') THEN 
            user_id := NEW.uid;
        ELSE
            user_id := OLD.uid;
        END IF;

        SELECT COALESCE(SUM(amount), 0) INTO total_in
        FROM income
        WHERE uid = user_id;
    
        SELECT COALESCE(SUM(amount), 0) INTO total_ex
        FROM expense
        WHERE uid = user_id;

        SELECT wid INTO wallet_id
        FROM users
        WHERE uid = user_id;
     
        UPDATE wallet
        SET 
            balance = total_in - total_ex, 
            total_income = total_in,
            total_expense = total_ex
        WHERE wid = wallet_id;

        RAISE NOTICE 'balance: %, income: %, expense: %', total_in - total_ex, total_in, total_ex;
    END;    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_wallet_trigger
AFTER INSERT OR UPDATE OR DELETE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_wallet();

CREATE TRIGGER update_wallet_trigger
AFTER INSERT OR UPDATE OR DELETE ON income
FOR EACH ROW
EXECUTE FUNCTION update_wallet();

CREATE FUNCTION update_expense_details() RETURNS TRIGGER AS $$
BEGIN
    DECLARE
        wallet_id TEXT;
        wallet_balance INTEGER;
        ratio FLOAT;
    BEGIN  
        IF (TG_OP = 'INSERT') THEN
            SELECT wid INTO wallet_id
            FROM users
            WHERE uid = NEW.uid;

            SELECT balance INTO wallet_balance
            FROM wallet
            WHERE wid = wallet_id;

            NEW.snapshot_balance := wallet_balance;
            NEW.percentage := 100 * NEW.amount::float / NEW.snapshot_balance::float;
        
        ELSIF (TG_OP = 'UPDATE') THEN 
            NEW.percentage := 100 * NEW.amount::float / OLD.snapshot_balance::float;
        END IF;            

        RAISE NOTICE 'expense ratio: %', NEW.percentage;
        RETURN NEW;
    END;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_expense_details
BEFORE INSERT OR UPDATE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_expense_details();

DROP FUNCTION get_transactions(user_id TEXT);
CREATE FUNCTION get_transactions(user_id TEXT) RETURNS TABLE (
    tid TEXT,
    t_category VARCHAR(30),
    t_amount INTEGER,
    t_time TIMESTAMP,
    t_description TEXT,
    t_percentage FLOAT,
    t_snapshot_balance INTEGER, 
    t_expense_level VARCHAR(10)
) AS $$
BEGIN 
    RETURN QUERY
    SELECT e.eid, ec.ec_name, e.amount, e.time, e.description, e.percentage, e.snapshot_balance, el.color_level
    FROM expense e
    JOIN expense_category ec ON ec.ecid = e.ecid
    JOIN expense_level el ON e.percentage BETWEEN el.percentage_range_lo AND el.percentage_range_hi
    WHERE e.uid = user_id

    UNION ALL
    
    SELECT i.iid, ic.ic_name, i.amount, i.time, i.description, NULL, NULL, NULL
    FROM income i
    JOIN income_category ic ON ic.icid = i.icid
    WHERE i.uid = user_id
    ORDER BY 4;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM get_transactions('u1');

-- create example of income
INSERT INTO income (uid, icid, amount, time, description) VALUES 
    ('u1', 'ic1', 1000000, '2020-01-01 00:17:00', 'Gaji');
INSERT INTO income (uid, icid, amount, time, description) VALUES 
    ('u1', 'ic2', 10000000, '2021-01-01 00:17:00', 'Gaji');

-- create example of expense
INSERT INTO expense (uid, ecid, amount, time, description) VALUES 
    ('u1', 'ec1', 30000, '2020-01-01 00:17:00', 'Makan di warteg');
INSERT INTO expense (uid, ecid, amount, time, description) VALUES 
    ('u1', 'ec4', 320000, '2018-01-01 19:17:10', 'Beli tiket konser');
INSERT INTO expense (uid, ecid, amount, time, description) VALUES 
    ('u1', 'ec3', 37000, '2018-01-01 00:17:00', 'Makan di warteg');
INSERT INTO expense (uid, ecid, amount, time, description) VALUES 
    ('u1', 'ec3', 5000000, '2018-01-01 00:17:00', 'Beli PS4');
    
-- query

-- NOTE YANG KURANG
-- ALgoritma penambahan point
-- Goal Setter
-- Peringatan