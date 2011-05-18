CREATE TABLE Student (
StudentId int PRIMARY KEY,
Sname varchar(50),
Major varchar(20),
DateDeclared int,
PRIMARY KEY (StudentId)
)
go

CREATE TABLE Advisor (
AdvisorName varchar(50),
AdvisorId int PRIMARY KEY,
MajorAdvised varchar(20),
PRIMARY KEY (AdvisorId)
)
go

CREATE TABLE Section (
ID int PRIMARY KEY,
CourseID int,
SectionID int,
Days_Time_Offered varchar(100),
TermOffered int,
YearOffered int,
PRIMARY KEY (ID)
)
go

CREATE TABLE Instructor (
InstSSN int PRIMARY KEY,
InstName varchar(50),
PRIMARY KEY (InstSSN)
)
go

CREATE TABLE Course (
CourseID int PRIMARY KEY,
Description varchar(2500),
Hours int,
PRIMARY KEY (CourseID)
)
go

CREATE TABLE Text (
ISBN int PRIMARY KEY,
AuthorName varchar(50),
PRIMARY KEY (ISBN)
)
go

CREATE TABLE Advisor_Student (
Advisor_AdvisorId int,
Student_StudentId int,
PRIMARY KEY (Advisor_AdvisorId, Student_StudentId),
FOREIGN KEY (Advisor_AdvisorId) REFERENCES Advisor(AdvisorId),
FOREIGN KEY (Student_StudentId) REFERENCES Student(StudentId)
)
go

CREATE TABLE Student_Section (
Student_StudentId int,
Section_ID int,
PRIMARY KEY (Student_StudentId, Section_ID),
FOREIGN KEY (Student_StudentId) REFERENCES Student(StudentId),
FOREIGN KEY (Section_ID) REFERENCES Section(ID)
)
go

ALTER TABLE Section
	ADD instructor_instssn int
go

ALTER TABLE Section
	ADD FOREIGN KEY (instructor_instssn) REFERENCES Instructor(InstSSN)
go

ALTER TABLE Course
	ADD section_id int
go

ALTER TABLE Course
	ADD FOREIGN KEY (section_id) REFERENCES Section(ID)
go

CREATE TABLE Course_Text (
Course_CourseID int,
Text_ISBN int,
PRIMARY KEY (Course_CourseID, Text_ISBN),
FOREIGN KEY (Course_CourseID) REFERENCES Course(CourseID),
FOREIGN KEY (Text_ISBN) REFERENCES Text(ISBN)
)
go

CREATE TABLE Instructor_Text (
Instructor_InstSSN int,
Text_ISBN int,
PRIMARY KEY (Instructor_InstSSN, Text_ISBN),
FOREIGN KEY (Instructor_InstSSN) REFERENCES Instructor(InstSSN),
FOREIGN KEY (Text_ISBN) REFERENCES Text(ISBN)
)
go

CREATE TABLE Instructor_Course (
Instructor_InstSSN int,
Course_CourseID int,
PRIMARY KEY (Instructor_InstSSN, Course_CourseID),
FOREIGN KEY (Instructor_InstSSN) REFERENCES Instructor(InstSSN),
FOREIGN KEY (Course_CourseID) REFERENCES Course(CourseID)
)
go

