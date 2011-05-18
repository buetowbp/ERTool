CREATE TABLE Person (
ID int PRIMARY KEY
)
go

CREATE TABLE Furniture (
ID int PRIMARY KEY
)
go

CREATE TABLE Person_Furniture (
ID int PRIMARY KEY
)
go

ALTER TABLE Person_Furniture
	ADD person_id int
go

ALTER TABLE Person_Furniture
	ADD FOREIGN KEY (person_id) REFERENCES Person(ID)
go

ALTER TABLE Person_Furniture
	ADD furniture_id int
go

ALTER TABLE Person_Furniture
	ADD FOREIGN KEY (furniture_id) REFERENCES Furniture(ID)
go

