CREATE DATABASE user_management;
USE user_management;

CREATE TABLE user(id INTEGER PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(30), 
lastName VARCHAR(30), email VARCHAR(30), phone VARCHAR(15), 
address VARCHAR(50), gender VARCHAR(6), age TINYINT, 
password VARCHAR(15), username VARCHAR(62) UNIQUE);

DROP TABLE user;

INSERT INTO messages (sender, receiver, message_text) VALUES (1, 22, 'hello, how are you?');

CALL getUsernames('maryiapaklon20');

CREATE TABLE messages(id INTEGER PRIMARY KEY AUTO_INCREMENT, sender INTEGER, receiver INTEGER, FOREIGN KEY (sender) REFERENCES user(id), FOREIGN KEY (receiver) REFERENCES user(id), time_sent TIMESTAMP NOT NULL DEFAULT current_timestamp, message_text TEXT);
DROP TABLE messages;
