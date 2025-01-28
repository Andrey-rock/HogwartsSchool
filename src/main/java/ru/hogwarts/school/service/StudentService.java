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
        Collection<Student> students = getAllStudents();
        return students.stream().filter(student -> student.getAge() == age)
                .toList();
    }


    public Student updateStudent(@NotNull Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }
}
