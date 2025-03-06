package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student addStudent(@NotNull Student student) {
        logger.info("Was invoked method for add student");
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        logger.info("Was invoked method for get student by id");
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = {}", id);
            return new NoSuchElementException("Не найден студент с id " + id);
        });
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get student by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentsByAgeMinMax(int minAge, int maxAge) {
        logger.info("Was invoked method for find students by age");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    public Student updateStudent(@NotNull Student student) {
        logger.info("Was invoked method for update student");
        Long id = student.getId();
        if (studentRepository.existsById(id)) {
            return studentRepository.save(student);
        }
        logger.error("Not found student with id = {}", id);
        throw  new NoSuchElementException("Не найден студент с id " + id);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentsByFacultyName(String name) {
        logger.info("Was invoked method for find students by faculty name");
        return studentRepository.findStudentsByFacultyName(name);
    }

    public Collection<Student> getStudentsByAgeAndFaculty(int age, String facultyName) {
        logger.info("Was invoked method for find students by age and faculty name");
        return studentRepository.findStudentsByAgeAndFacultyName(age, facultyName);
    }

    public int getAllStudentsCount() {
        logger.info("Was invoked method for get count all students");
        return studentRepository.getAllStudentsCount();
    }

    public double getAvgAge() {
        logger.info("Was invoked method for get avg age for students");
        return studentRepository.getAvgAge();
    }

    public Collection<Student> getFiveLastStudent() {
        logger.info("Was invoked method for get five last student");
        return studentRepository.getFiveLastStudent();
    }

    public Collection<Student> getStudentsWithNameStartingWithA() {
        logger.info("Was invoked method for get students with name starting with A");
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().startsWith("А"))
                .peek(s -> {
                    String name = s.getName();
                    s.setName(name.toUpperCase());
                })
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average age for students");
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(Double.NaN);
    }
}
