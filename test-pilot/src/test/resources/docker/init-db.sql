CREATE TABLE IF NOT EXISTS ROOMS (
    roomid SERIAL PRIMARY KEY,
    room_name varchar(255),
    type varchar(255),
    beds int,
    accessible boolean,
    image varchar(2000),
    description varchar(2000),
    features varchar(100)[],
    roomPrice int
);

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