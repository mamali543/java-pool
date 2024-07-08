INSERT INTO chat.users (login, password) VALUES ('user1', 'password1');
INSERT INTO chat.users (login, password) VALUES ('user2', 'password2');
INSERT INTO chat.users (login, password) VALUES ('user3', 'password3');
INSERT INTO chat.users (login, password) VALUES ('user4', 'password4');
INSERT INTO chat.users (login, password) VALUES ('user5', 'password5');


INSERT INTO chat.chatrooms (name, owner_id) VALUES ('Java Chat', 1);
INSERT INTO chat.chatrooms (name, owner_id) VALUES ('Python Chat', 2);
INSERT INTO chat.chatrooms (name, owner_id) VALUES ('C++ Chat', 3);
INSERT INTO chat.chatrooms (name, owner_id) VALUES ('JavaScript Chat', 4);
INSERT INTO chat.chatrooms (name, owner_id) VALUES ('Ruby Chat', 5);

INSERT INTO chat.message (author_id, room_id, content, datetime) VALUES (1, 1, 'Hello Java World!', '2024-05-07 12:00:00');
INSERT INTO chat.message (author_id, room_id, content, datetime) VALUES (2, 1, 'Hi, Java folks!', '2024-05-07 12:05:00');
INSERT INTO chat.message (author_id, room_id, content, datetime) VALUES (3, 2, 'Python is great!', '2024-05-07 12:10:00');
INSERT INTO chat.message (author_id, room_id, content, datetime) VALUES (4, 3, 'C++ is powerful!', '2024-05-07 12:15:00');
INSERT INTO chat.message (author_id, room_id, content, datetime) VALUES (5, 4, 'JavaScript is versatile!', '2024-05-07 12:20:00');

INSERT INTO chat.user_chatrooms (user_id, chatroom_id) VALUES (1, 1), (2, 1), (1, 2);
