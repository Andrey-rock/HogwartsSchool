ALTER TABLE student
ADD CONSTRAINT age_constraint CHECK (age >= 16);

ALTER TABLE student
ALTER  COLUMN age SET NOT NULL,
    ADD CONSTRAINT name_constraint UNIQUE (name);

ALTER TABLE faculty
ADD CONSTRAINT faculty_constraint UNIQUE (colour, name);

ALTER TABLE student
ALTER COLUMN age SET DEFAULT 20;