package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    public Student addStudent(@NotNull Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    public Student getStudent(long id) {
        return students.get(id);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Collection<Student> getStudentsByAge(int age) {
        Collection<Student> students = getAllStudents();
        return students.stream().filter(student -> student.getAge() == age)
                .toList();
    }


    public Student updateStudent(@NotNull Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }
}
