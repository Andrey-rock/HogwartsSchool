package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id) {
        Student student = studentService.getStudent(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public Collection<Student> getAllStudents(@RequestParam(required = false) String age) {
        if (age == null) {
            return studentService.getAllStudents();
        }
        return studentService.getStudentsByAge(Integer.parseInt(age));
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student);
        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable int id) {
        Student deletedStudent = studentService.deleteStudent(id);
        if (deletedStudent != null) {
            return ResponseEntity.ok(deletedStudent);
        }
        return ResponseEntity.notFound().build();
    }
}
