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
