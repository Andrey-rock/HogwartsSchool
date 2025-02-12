CREATE TABLE IF NOT EXISTS public.STUDENT
(
    id bigint NOT NULL,
    age integer NOT NULL,
    name character varying(255),
    faculty_id bigint,
    avatar_id bigint);

INSERT INTO STUDENT(id, age, name) values (1, 11, 'Garry');
INSERT INTO STUDENT(id, age, name) values (2, 12, 'Germiona');
INSERT INTO STUDENT(id, age, name) values (3, 13, 'Ron');







