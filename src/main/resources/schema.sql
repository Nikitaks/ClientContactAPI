CREATE TABLE CLIENTS (ID INT AUTO_INCREMENT PRIMARY KEY, 
NAME VARCHAR(255) UNIQUE NOT NULL);

CREATE TABLE CONTACT_TYPES (ID INT PRIMARY KEY,
CONTACT_TYPE VARCHAR(255) NOT NULL);

CREATE TABLE CONTACTS (ID INT AUTO_INCREMENT PRIMARY KEY,
CLIENT_ID INT NOT NULL,
CONTACT_TYPE_ID INT NOT NULL,
CONTACT VARCHAR(255) NOT NULL,
FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS (ID),
FOREIGN KEY (CONTACT_TYPE_ID) REFERENCES CONTACT_TYPES (ID));