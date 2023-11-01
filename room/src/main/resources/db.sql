CREATE TABLE IF NOT EXISTS ROOMS (
    roomid SERIAL PRIMARY KEY,
    room_name VARCHAR(255),
    type VARCHAR(255),
    beds INT,
    accessible BOOLEAN,
    image VARCHAR(2000),
    description VARCHAR(2000),
    features VARCHAR(100)[],
    roomPrice INT
);
