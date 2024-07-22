DROP SCHEMA IF EXISTS chat CASCADE;
CREATE SCHEMA IF NOT EXISTS chat;

CREATE TABLE IF NOT EXISTS chat.users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS chat.chatrooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id INTEGER,
    FOREIGN KEY (owner_id) REFERENCES chat.users(id),
);

CREATE TABLE IF NOT EXISTS chat.message (
    id SERIAL PRIMARY KEY,
    author_id INTEGER,
    room_id INTEGER ,
    content VARCHAR(255) NOT NULL,
    datetime TIMESTAMP NOT NULL,
    FOREIGN KEY (author_id) REFERENCES chat.users(id),
    FOREIGN KEY (room_id) REFERENCES chat.chatrooms(id)
);

CREATE TABLE IF NOT EXISTS chat.user_chatrooms (
    user_id INTEGER NOT NULL,
    chatroom_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, chatroom_id),
    FOREIGN KEY (user_id) REFERENCES chat.users(id),
    FOREIGN KEY (chatroom_id) REFERENCES chat.chatrooms(id)
);