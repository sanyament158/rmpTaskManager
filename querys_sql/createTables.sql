CREATE TABLE Role(
    Id int AUTO_INCREMENT PRIMARY KEY,
    Name varchar(50) UNIQUE);
-- was runned
CREATE TABLE User(
	Id int AUTO_INCREMENT PRIMARY KEY,
    Username varchar(100) UNIQUE,
    Fname varchar(50),
    Lname varchar(50),
    IdRole int,
    FOREIGN KEY (IdRole) REFERENCES Role(Id)
);
-- was runned
CREATE TABLE TaskStatus(
    Id int AUTO_INCREMENT PRIMARY KEY,
    Name varchar(50) UNIQUE);
CREATE TABLE TaskCategory(
    Id int AUTO_INCREMENT PRIMARY KEY,
    Name varchar(50) UNIQUE);
CREATE TABLE TaskImportance(
    Id int AUTO_INCREMENT PRIMARY KEY,
    Name varchar(50) UNIQUE,
    Grade int UNIQUE);
-- was runned

create table Tasks(
    Id int AUTO_INCREMENT PRIMARY KEY,
    IdOwner int,
    IdStatus int,
    IdCategory int,
    IdImportance int,
    Title varchar(100),
    Description text,
	FOREIGN KEY (IdOwner) REFERENCES User(Id),
    FOREIGN KEY (IdStatus) REFERENCES TaskStatus(Id),
    FOREIGN KEY (IdCategory) REFERENCES TaskCategory(Id),
    FOREIGN KEY (IdImportance) REFERENCES TaskImportance(Id)
);
--was runned