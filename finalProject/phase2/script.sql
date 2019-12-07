DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Hotel CASCADE;
DROP TABLE IF EXISTS Staff CASCADE;
DROP TABLE IF EXISTS Room CASCADE;
DROP TABLE IF EXISTS Booking CASCADE;
DROP TABLE IF EXISTS Assigned CASCADE;
DROP TABLE IF EXISTS Maintenance_Company CASCADE;
DROP TABLE IF EXISTS Requests CASCADE;
DROP TABLE IF EXISTS Repair CASCADE;

CREATE TABLE Hotel(HotelID char(40) NOT NULL, Address char(40) NOT NULL, Manages char(40) NOT NULL, PRIMARY KEY (HotelID));

CREATE TABLE Staff(SSN char(40) NOT NULL, fName char(40) NOT NULL, lName char(40) NOT NULL, Address char(40) NOT NULL, Employs char(40) NOT NULL, PRIMARY KEY(SSN), FOREIGN KEY(Employs) REFERENCES Hotel(HotelID));

ALTER TABLE Hotel ADD FOREIGN KEY(Manages) REFERENCES Staff(SSN);

CREATE TABLE Room(RoomNo char(40) NOT NULL, Type char(40) NOT NULL, HotelID char(40) NOT NULL, PRIMARY KEY(RoomNo, HotelID), FOREIGN KEY(HotelID) REFERENCES Hotel(HotelID) ON DELETE CASCADE);

CREATE TABLE Customer(CustomerID char(40), fName char(40), lName char(40), Address char(40), phNo char(40), DOB char(40), Gender char(40), PRIMARY KEY(CustomerID));

CREATE TABLE Maintenance_Company(cmpID char(40) NOT NULL, Name char(40) NOT NULL, Address char(40) NOT NULL, Certified boolean NOT NULL, PRIMARY KEY(cmpID));

CREATE TABLE Booking(date char(40), numPeople integer, price char(40), RoomNo char(40), HotelID char(40), CustomerID char(40), PRIMARY KEY(CustomerID, HotelID, RoomNo), FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID), FOREIGN KEY (RoomNo, HotelID) REFERENCES Room(RoomNo, HotelID));

CREATE TABLE Repair(day date NOT NULL, description char(40) NOT NULL, repair_type char(40) NOT NULL, cmpID char(40) NOT NULL, HotelID char(40) NOT NULL, RoomNo char(40) NOT NULL, PRIMARY KEY(cmpID, HotelID, RoomNo), FOREIGN KEY(cmpID) REFERENCES Maintenance_Company(cmpID), FOREIGN KEY(RoomNo, HotelID) REFERENCES Room(RoomNo, HotelID));

CREATE TABLE Assigned(SSN char(40) NOT NULL, HotelID char(40) NOT NULL, RoomNo char(40) NOT NULL, PRIMARY KEY(SSN, HotelID, RoomNo), FOREIGN KEY(SSN) REFERENCES Staff(SSN), FOREIGN KEY(RoomNo, HotelID) REFERENCES Room(RoomNo, HotelID));

CREATE TABLE Requests(day date, description char(40), SSN char(40), cmpID char(40), RoomNo char(40), HotelID char(40), PRIMARY KEY(SSN, cmpID, RoomNo, HotelID), FOREIGN KEY(SSN) REFERENCES Staff(SSN), FOREIGN KEY(cmpID) REFERENCES Maintenance_Company(cmpID), FOREIGN KEY (RoomNo, HotelID) REFERENCES Room(RoomNo, HotelID));
