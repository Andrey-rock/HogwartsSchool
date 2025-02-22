package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(@NotNull Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAge(int age) {

        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentsByAgeMinMax(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    public Student updateStudent(@NotNull Student student) {
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentsByFacultyName(String name) {
        return studentRepository.findStudentsByFacultyName(name);
    }

    public Collection<Student> getStudentsByAgeAndFaculty(int age, String facultyName) {
        return studentRepository.findStudentsByAgeAndFacultyName(age, facultyName);
    }

    public int getAllStudentsCount() {
        return studentRepository.getAllStudentsCount();
    }

    public double getAvgAge() {
        return studentRepository.getAvgAge();
    }

    public Collection<Student> getFiveLastStudent() {
        return studentRepository.getFiveLastStudent();
    }
}
