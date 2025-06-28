ALTER TABLE users
    ADD username VARCHAR(255) NULL;

ALTER TABLE users
    DROP COLUMN first_name;

ALTER TABLE users
    DROP COLUMN last_name;