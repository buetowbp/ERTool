CREATE TABLE School (
ID int PRIMARY KEY
)
go

CREATE TABLE Class (
ID int PRIMARY KEY
)
go

ALTER TABLE School
	ADD FOREIGN KEY (class_id) reference Class(ID)
go

