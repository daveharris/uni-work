DROP TABLE lec_course;
DROP TABLE lecturers CASCADE;
DROP TABLE courses CASCADE;
DROP TABLE departments CASCADE;

CREATE TABLE departments (
DptID        char(15),
Dpt_Name     char(15)      NOT NULL,
CONSTRAINT courID CHECK (DptID >= 0),
CONSTRAINT departments_pk PRIMARY KEY (DptID)
);

CREATE TABLE lecturers (
LecID           int,
Lec_Name        char(15)      NOT NULL,
DptID           char(15),
CONSTRAINT lecID CHECK (LecID >= 0),
CONSTRAINT DptID_fk FOREIGN KEY (DptID) REFERENCES departments
         ON DELETE SET NULL       ON UPDATE CASCADE,
CONSTRAINT lecturers_pk PRIMARY KEY (LecID)
);

CREATE TABLE courses (
CourID        char(15),
Cour_Name     char(15)      NOT NULL,
DptID         char(15),
CONSTRAINT courID CHECK (courID >= 0),
CONSTRAINT DptID_fk FOREIGN KEY (DptID) REFERENCES departments
         ON DELETE SET NULL       ON UPDATE CASCADE,
CONSTRAINT courses_pk PRIMARY KEY (CourID)
);


CREATE TABLE lec_course (
LecID       int,
CourID      char(15),
CONSTRAINT lec_cour_lecturer_fk FOREIGN KEY (LecID) REFERENCES lecturers
         ON DELETE CASCADE       ON UPDATE CASCADE,
CONSTRAINT lec_cour_course_fk FOREIGN KEY (CourID) REFERENCES courses
         ON DELETE CASCADE       ON UPDATE CASCADE,
CONSTRAINT lec_course_pk PRIMARY KEY (LecID, CourID)
);