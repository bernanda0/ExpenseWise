-- EXPENSE WISE SQL DUMP
-- Version 1.0

-- Sequence for serial
CREATE SEQUENCE uid_seq START 1;
CREATE SEQUENCE wid_seq START 1;
CREATE SEQUENCE eid_seq START 1;
CREATE SEQUENCE iid_seq START 1;
CREATE SEQUENCE ecid_seq START 1;
CREATE SEQUENCE icid_seq START 1;

-- Session 
CREATE TABLE session (
    sid TEXT PRIMARY KEY NOT NULL,
    sess JSON NOT NULL,
    expire TIMESTAMP NOT NULL
);

-- Enumeration for e_wallet
CREATE TYPE e_wallet AS ENUM ('OVO', 'GOPAY', 'DANA');
CREATE TABLE e_wallet_token (
    id SERIAL PRIMARY KEY,
    type e_wallet,
    token TEXT
); 

-- Wallet table 
CREATE TABLE wallet (
    wid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('w', nextval('wid_seq')),
    total_income INTEGER DEFAULT 0,
    total_expense INTEGER DEFAULT 0,
    balance INTEGER DEFAULT 0,
    connected_wallet e_wallet [] DEFAULT '{}' :: e_wallet []
);

-- User table
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

-- Expense category table
CREATE TABLE expense_category (
    ecid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('ec', nextval('ecid_seq')),
    ec_name VARCHAR(30) UNIQUE NOT NULL
);

-- Expense table
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

-- Expense level table
CREATE TABLE expense_level (
    percentage_range_lo FLOAT NOT NULL,
    percentage_range_hi FLOAT NOT NULL,
    color_level VARCHAR(10) UNIQUE NOT NULL,
    PRIMARY KEY (percentage_range_lo, percentage_range_hi)
);

-- Income category table
CREATE TABLE income_category (
    icid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('ic', nextval('icid_seq')),
    ic_name VARCHAR(30) UNIQUE NOT NULL
);

-- Income table
CREATE TABLE income (
    iid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('i', nextval('iid_seq')),
    icid TEXT REFERENCES income_category (icid) ON DELETE CASCADE,
    uid TEXT REFERENCES users (uid) ON DELETE CASCADE,
    amount INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    description TEXT
);

-- User rank table
CREATE TABLE user_rank (
    point_range_lo INTEGER NOT NULL,
    point_range_hi INTEGER NOT NULL,
    title VARCHAR(30) UNIQUE NOT NULL,
    PRIMARY KEY (point_range_lo, point_range_hi)
);

-- Base record for the reference table
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
    (51, 9999, 'Red');

INSERT INTO
    user_rank
VALUES
    (-9999, -1, 'Waster'),
    (0, 999, 'Beginner'),
    (1000, 2999, 'Saver'),
    (3000, 5999, 'Wise'),
    (6000, 9999, 'Lord');


-- Function definition

-- A trigger function to update the wallet balance, total income, and total expense 
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

-- Add a trigger that will update the wallet balance, total income, and total expense
CREATE TRIGGER update_wallet_trigger
AFTER INSERT OR UPDATE OR DELETE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_wallet();

CREATE TRIGGER update_wallet_trigger
AFTER INSERT OR UPDATE OR DELETE ON income
FOR EACH ROW
EXECUTE FUNCTION update_wallet();

-- A trigger function to update the expense percentage and snapshot balance
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

-- Add a trigger that will update the expense percentage and snapshot balance
CREATE TRIGGER trigger_expense_details
BEFORE INSERT OR UPDATE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_expense_details();

-- A function to return all the transactions of a user
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

-- A trigger function that will update the user points with this rule
-- every expense that has 'green' level will increase 10 points
-- every expense that has 'yellow' level will increase 4 points
-- every expense that has 'orange' level will increase 2 points
-- every expense that has 'red' level will reduce 5 points
-- and every expense that has 'black' level will reduce 10 points
DROP FUNCTION update_user_points() CASCADE;
CREATE FUNCTION update_user_points() RETURNS TRIGGER AS $$
BEGIN
    DECLARE
        user_id TEXT;
        user_points INTEGER;
        expense_level VARCHAR(10);
    BEGIN
        user_id := NEW.uid;
        expense_level := (
            SELECT el.color_level
            FROM expense e
            JOIN expense_level el ON e.percentage BETWEEN el.percentage_range_lo AND el.percentage_range_hi
            WHERE e.eid = NEW.eid
        );

        IF (expense_level = 'Green') THEN
            RAISE NOTICE 'user will get 10 points';
            UPDATE users
            SET points = points + 10
            WHERE uid = user_id;
        ELSIF (expense_level = 'Yellow') THEN
            RAISE NOTICE 'user will get 4 points';
            UPDATE users
            SET points = points + 4
            WHERE uid = user_id;
        ELSIF (expense_level = 'Orange') THEN
            RAISE NOTICE 'user will get 2 points';
            UPDATE users
            SET points = points + 2
            WHERE uid = user_id;
        ELSIF (expense_level = 'Red') THEN
            RAISE NOTICE 'user will lose 10 points';
            UPDATE users
            SET points = points - 10
            WHERE uid = user_id;
        ELSIF (expense_level = 'Black') THEN
            RAISE NOTICE 'user will lose 15 points';
            UPDATE users
            SET points = points - 15
            WHERE uid = user_id;
        END IF;
    END;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add a trigger that will update the user points
CREATE TRIGGER update_user_points_trigger
AFTER INSERT OR UPDATE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_user_points();

-- A function that will return the summary of a user 
CREATE FUNCTION get_user(user_id TEXT) RETURNS TABLE (
    username VARCHAR(30),
    email VARCHAR(30),
    img TEXT,
    phone_number VARCHAR(16),
    occupation TEXT,
    points INTEGER,
    user_rank VARCHAR(30)
) AS $$
BEGIN
    RETURN QUERY
    SELECT u.username, u.email, u.img, u.phone_number, u.occupation, u.points, ur.title
    FROM users u
    JOIN user_rank ur ON u.points BETWEEN ur.point_range_lo AND ur.point_range_hi
    WHERE u.uid = user_id;
END;
$$ LANGUAGE plpgsql;

-- A function that will return the ranged time from the oldest transaction to the newest transaction
CREATE FUNCTION get_time_range(user_id TEXT) RETURNS TABLE (
    oldest_time TIMESTAMP,
    newest_time TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT MIN(t_time), MAX(t_time)
    FROM get_transactions(user_id);
END;
$$ LANGUAGE plpgsql;

-- NOT YET IMPLEMENTED : Goal Setter





-- BELOW IS DEPRECATED AND TESTED QUERY
-- A function that will return all the income category and its percentage
CREATE FUNCTION icat_percentage (user_id TEXT) RETURNS TABLE (
    ic_name VARCHAR(30),
    percentage FLOAT
) AS $$
BEGIN
    RETURN QUERY
    SELECT ic.ic_name, SUM(i.amount)::float / (SELECT SUM(amount) FROM income WHERE uid = user_id) * 100
    FROM income i
    JOIN income_category ic ON ic.icid = i.icid
    WHERE i.uid = user_id
    GROUP BY ic.ic_name
    ORDER BY 2 DESC;
END;
$$ LANGUAGE plpgsql;

-- A function that will return all the expense category and its percentage
CREATE FUNCTION ecat_percentage (user_id TEXT) RETURNS TABLE (
    ec_name VARCHAR(30),
    percentage FLOAT
) AS $$
BEGIN
    RETURN QUERY
    SELECT ec.ec_name, SUM(e.amount)::float / (SELECT SUM(amount) FROM expense WHERE uid = user_id) * 100
    FROM expense e
    JOIN expense_category ec ON ec.ecid = e.ecid
    WHERE e.uid = user_id
    GROUP BY ec.ec_name
    ORDER BY 2 DESC;
END;
$$ LANGUAGE plpgsql;

-- create example of income
INSERT INTO income (uid, icid, amount, time, description) VALUES 
    ('u1', 'ic1', 15000000, '2022-01-01 00:17:00', 'Bonus');
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
    ('u1', 'ec4', 10000000, '2022-01-01 00:17:00', 'Beli S23');