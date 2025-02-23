package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    Collection<Student> findStudentsByFacultyName(String facultyName);

    Collection<Student> findStudentsByAgeAndFacultyName(int age, String facultyName);

    @Query(value = "select count(*) from student;", nativeQuery = true)
    int getAllStudentsCount();

    @Query(value = "select avg(age) from student;", nativeQuery = true)
    double getAvgAge();

    String QUERY_GET_FIVE_STUDENTS = "select * from student " +
            "order by id limit 5 offset (select count(*) - 5 from student);";

    @Query(value = QUERY_GET_FIVE_STUDENTS, nativeQuery = true)
    Collection<Student> getFiveLastStudent();


}
