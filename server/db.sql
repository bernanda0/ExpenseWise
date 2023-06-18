-- EXPENSE WISE SQL DUMP
-- Version 1.0

-- Sequence for serial
CREATE SEQUENCE uid_seq START 1;
CREATE SEQUENCE wid_seq START 1;
CREATE SEQUENCE eid_seq START 1;
CREATE SEQUENCE ugid_seq START 1;
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

-- Goal table
CREATE TABLE user_goal (
    ugid TEXT PRIMARY KEY NOT NULL DEFAULT CONCAT('ug', nextval('ugid_seq')),
    uid TEXT REFERENCES users (uid) ON DELETE CASCADE,
    p_expense INTEGER DEFAULT 0,
    goal_expense INTEGER NOT NULL,
    start_period TIMESTAMP NOT NULL DEFAULT NOW(),
    end_period DATE NOT NULL,
    status TEXT NOT NULL DEFAULT 'ongoing'
);

-- Remove unique constrain from uid
ALTER TABLE user_goal DROP CONSTRAINT user_goal_uid_key;

-- Change the type of start_period to timestamp with default value
ALTER TABLE user_goal ALTER COLUMN start_period TYPE TIMESTAMP USING start_period::TIMESTAMP;

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

-- Trigger and function
-- every time there is new or updated expense, the trigger will update the p_expense in user_goal table
DROP FUNCTION update_p_expense() CASCADE;

CREATE FUNCTION update_p_expense() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        UPDATE user_goal
        SET p_expense = p_expense + NEW.amount
        WHERE uid = NEW.uid;
    ELSIF (TG_OP = 'UPDATE') THEN
        UPDATE user_goal
        SET p_expense = p_expense + NEW.amount - OLD.amount
        WHERE uid = NEW.uid;
    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE user_goal
        SET p_expense = p_expense - OLD.amount
        WHERE uid = OLD.uid;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_p_expense_trigger
AFTER INSERT OR UPDATE OR DELETE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_p_expense();

DROP FUNCTION update_goal_status() CASCADE;
CREATE FUNCTION update_goal_status() RETURNS TRIGGER AS $$
BEGIN 
    NEW.status := (
        CASE 
            WHEN (NEW.end_period >= NOW()::DATE AND NEW.p_expense <= NEW.goal_expense) THEN 'ongoing'
            WHEN (NEW.end_period >= NOW()::DATE AND NEW.p_expense  > NEW.goal_expense) THEN 'failed'
            WHEN (NEW.end_period  < NOW()::DATE AND NEW.p_expense <= NEW.goal_expense) THEN 'success'
            WHEN (NEW.end_period  < NOW()::DATE AND NEW.p_expense  > NEW.goal_expense) THEN 'failed'
        END
    );
    OLD.status := NEW.status;
    RAISE NOTICE 'goal status: %', NEW.status;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_goal_status_trigger
BEFORE UPDATE ON user_goal
FOR EACH ROW
EXECUTE FUNCTION update_goal_status();

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
DROP FUNCTION update_user_points(user_id TEXT, expense_id TEXT) CASCADE;

-- CREATE NON TRIGGER FUNCTION that will update the user points
CREATE FUNCTION update_user_points(user_id TEXT, new_percentage FLOAT) RETURNS VOID AS $$
BEGIN
    DECLARE
        expense_level VARCHAR(10);
        added_points INTEGER;
    BEGIN
        expense_level := (
           SELECT color_level
           FROM expense_level
           WHERE new_percentage BETWEEN percentage_range_lo AND percentage_range_hi
        );

        IF (expense_level = 'Green') THEN
            RAISE NOTICE 'user will get 10 points';
            added_points := 10;
        ELSIF (expense_level = 'Yellow') THEN
            RAISE NOTICE 'user will get 4 points';
            added_points := 4;
        ELSIF (expense_level = 'Orange') THEN
            RAISE NOTICE 'user will get 2 points';
            added_points := 2;
        ELSIF (expense_level = 'Red') THEN
            RAISE NOTICE 'user will lose 5 points';
            added_points := -5;
        ELSIF (expense_level = 'Black') THEN
            RAISE NOTICE 'user will lose 10 points';
            added_points := -10;
        END IF;

        UPDATE users
        SET points = points + added_points
        WHERE uid = user_id;
    END;
END;
$$ LANGUAGE plpgsql;

-- A TRIGGER FUNCTION that will update the user points
DROP FUNCTION update_user_points_tr() CASCADE;
CREATE FUNCTION update_user_points_tr() RETURNS TRIGGER AS $$
BEGIN
    PERFORM update_user_points(NEW.uid, NEW.percentage);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add a trigger that will update the user points (INSERTION)
CREATE TRIGGER update_user_points_trigger
AFTER INSERT OR UPDATE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_user_points_tr();

-- This function is when the expense is edited, 
-- point of the user will be subtracted by the old expense level 
-- and added by the new expense level
DROP FUNCTION adjust_user_point() CASCADE;
CREATE FUNCTION adjust_user_point() RETURNS TRIGGER AS $$
BEGIN
    DECLARE
        old_el_color VARCHAR(10); 
        old_el_point INTEGER;
        new_el_color VARCHAR(10);   
        new_el_point INTEGER;
    BEGIN 
        old_el_color := (
            SELECT el.color_level
            FROM expense e
            JOIN expense_level el ON e.percentage BETWEEN el.percentage_range_lo AND el.percentage_range_hi
            WHERE e.eid = OLD.eid
        );

        IF (old_el_color = 'Green') THEN
            old_el_point := 10;
        ELSIF (old_el_color = 'Yellow') THEN
            old_el_point := 4;
        ELSIF (old_el_color = 'Orange') THEN
            old_el_point := 2;
        ELSIF (old_el_color = 'Red') THEN
            old_el_point := -10;
        ELSIF (old_el_color = 'Black') THEN
            old_el_point := -15;
        END IF;

        UPDATE users
        SET points = points - old_el_point
        WHERE uid = OLD.uid;

        RAISE NOTICE 'user will lose % points', old_el_point;
    END;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add a trigger that will update the user points (UPDATE)
CREATE TRIGGER adjust_user_point_trigger
BEFORE UPDATE ON expense
FOR EACH ROW
EXECUTE FUNCTION adjust_user_point();

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

---
INSERT INTO user_goal (uid, goal_expense, end_period) VALUES ('u1', 1000000, '2023-6-18');
INSERT INTO expense (uid, ecid, amount, time, description) VALUES ('u1', 'ec1', 60000, '2021-12-23 00:17:00', 'Beli elektronik');
UPDATE expense SET amount = 1200000 WHERE eid = 'e16';
DELETE FROM user_goal WHERE uid = 'u1';

-- get averafe expense
SELECT AVG(amount) FROM expense WHERE uid = 'u1';
-- join expense and its category then get the distinct expense category
SELECT DISTINCT ec.ec_name FROM expense e JOIN expense_category ec ON ec.ecid = e.ecid WHERE e.uid = 'u1';
-- select the number of transaction each expense category
SELECT ec.ec_name, COUNT(ec.ec_name) FROM expense e JOIN expense_category ec ON ec.ecid = e.ecid WHERE e.uid = 'u1' GROUP BY ec.ec_name;

-- UPDATE POINT OF USER
UPDATE users SET points = 97 WHERE uid = 'u1';

