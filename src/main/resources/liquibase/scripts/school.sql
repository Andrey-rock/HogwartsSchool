-- liquibase formatted sql
-- changeset andrey-rock:1
CREATE INDEX student_name_index ON student(name);

-- changeset andrey-rock:2
CREATE INDEX faculty_search_index ON faculty(name, colour);
