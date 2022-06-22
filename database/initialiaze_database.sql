DROP user IF EXISTS 'cs161020'@'localhost';
CREATE user 'cs161020' @'localhost';
ALTER USER 'cs161020'@'localhost' IDENTIFIED BY '1234';

DROP DATABASE IF EXISTS recipes_db;
CREATE DATABASE recipes_db; 

GRANT ALL PRIVILEGES ON `recipes_db`.* TO 'cs161020'@'localhost' WITH GRANT OPTION;

