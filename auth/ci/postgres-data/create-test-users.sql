CREATE TABLE IF NOT EXISTS TOKENS (
    tokenid SERIAL PRIMARY KEY,
    token VARCHAR(255),
    expiry TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ACCOUNTS (
    accountid SERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255)
);

COPY ACCOUNTS(username, password)
FROM '/docker-entrypoint-initdb.d/test-users.csv'
DELIMITER ',';