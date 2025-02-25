SELECT student.name, student.age, faculty.name
FROM student LEFT JOIN faculty on faculty.id = student.faculty_id;

SELECT student.name, avatar_id
FROM student INNER JOIN public.avatar a on student.id = a.student_id;
