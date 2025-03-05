package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
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
    public Collection<Student> getStudents(@RequestParam(required = false) String age,
                                           @RequestParam(required = false) String facultyName) {
        if ((age != null) && (facultyName == null)) {
            return studentService.getStudentsByAge(Integer.parseInt(age));
        }
        if ((facultyName != null) && (age == null)) {
            return studentService.findStudentsByFacultyName(facultyName);
        }
        if (age != null) {
            return studentService.getStudentsByAgeAndFaculty(Integer.parseInt(age), facultyName);
        }

        return studentService.getAllStudents();
    }

    @GetMapping("/minmax")
    public Collection<Student> finStudentsByAgeMinMax(@RequestParam int min,
                                                      @RequestParam int max) {

        return studentService.findStudentsByAgeMinMax(min, max);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/count")
    public int getAllStudentsCount() {
        return studentService.getAllStudentsCount();
    }

    @GetMapping("/avg_age")
    public double getAvgAge() {
        return studentService.getAvgAge();
    }

    @GetMapping("/5_last")
    public Collection<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudent();
    }
}
