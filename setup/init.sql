use login;
DROP TABLE users;
DROP TABLE user;
DROP TABLE submission;
DROP TABLE confirmation_token;
CREATE TABLE users 
(
	id INT PRIMARY KEY AUTO_INCREMENT,
	email VARCHAR(50), 
	enabled bool , 
	locked bool, 
	name VARCHAR(50), 
	password VARCHAR(512), 
	surname VARCHAR(50),
       	user_role int
);
CREATE TABLE confirmation_token
(
	id INT PRIMARY KEY AUTO_INCREMENT,
	confirmation_token VARCHAR(50), 
	created_date date, 
	user_id INT
);
CREATE TABLE submission 
(
	    username VARCHAR(50),
	    jsoninfo VARCHAR(500),
            uuid VARCHAR(50),
            ready  bool,
            tm  datetime	
);
CREATE TABLE user 
(
	    id INT PRIMARY KEY AUTO_INCREMENT,
	    username VARCHAR(50),
	    password VARCHAR(50) 
);

INSERT INTO user (username, password) values ("test","test");
commit;
