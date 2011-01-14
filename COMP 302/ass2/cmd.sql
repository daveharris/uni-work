DROP TABLE customer CASCADE;
DROP TABLE loaned_book CASCADE;
DROP TABLE Book CASCADE;
DROP TABLE Author CASCADE;
DROP TABLE Book_Author CASCADE;


CREATE TABLE Customer (
CustomerID      int         NOT NULL        DEFAULT 0       CHECK (CustomerID >= 0),
L_Name          char(15)    NOT NULL,
F_Name          char(15),
City            char(15)    DEFAULT 'Wellington'            CHECK (City IN ('Wellington','Upper Hutt','Lower Hutt')),
CONSTRAINT Customer_PK PRIMARY KEY(CustomerID)
);


CREATE TABLE Book (
ISBN            int         NOT NULL        DEFAULT 0       CHECK (ISBN >= 0),
Title           char(60)    NOT NULL,
Edition_No      smallint                    DEFAULT 1       CHECK (Edition_No >= 0),
NumOfCop        smallint    NOT NULL        DEFAULT 1,
NumLeft         smallint    NOT NULL        DEFAULT 1,
CONSTRAINT ISBN_PK PRIMARY KEY(ISBN)
);


CREATE TABLE Loaned_book (
CustomerId      int         NOT NULL        DEFAULT 0       CHECK (CustomerID >= 0)
CONSTRAINT LBFKCUST  REFERENCES Customer
            ON DELETE RESTRICT      ON UPDATE RESTRICT,
DueDate         date,
ISBN            int         NOT NULL        DEFAULT 0       CHECK (ISBN >= 0)
CONSTRAINT LBFKBOOK REFERENCES Book
            ON DELETE RESTRICT      ON UPDATE CASCADE,
CONSTRAINT Loaned_book_PK PRIMARY KEY(CustomerID, ISBN)
);


CREATE TABLE Author (
AuthorId        int         NOT NULL        DEFAULT 0       CHECK (AuthorId >= 0),
Name            char(15),
Surname         char(15)    NOT NULL,
CONSTRAINT Author_PK PRIMARY KEY(AuthorId)
);


CREATE TABLE Book_Author (     
ISBN            int         NOT NULL        DEFAULT 0       CHECK (ISBN >= 0)
CONSTRAINT BAFKBK REFERENCES Book
            ON DELETE SET DEFAULT       ON UPDATE CASCADE,
AuthorId        int         NOT NULL        DEFAULT 0       CHECK (AuthorId >= 0)
CONSTRAINT LBFKAU REFERENCES Author
            ON DELETE SET DEFAULT       ON UPDATE CASCADE,
AuthorSeqNo     smallint                    DEFAULT 1       CHECK (AuthorSeqNo >= 0),
CONSTRAINT Book_Author_PK PRIMARY KEY(ISBN, AuthorId)
);