package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("students")
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
    public String getAvgAge() {
        return String.format("%.2f", studentService.getAvgAge());
    }

    @GetMapping("/5_last")
    public Collection<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudent();
    }

    @GetMapping("/name_start_a")
    public Collection<Student> getStudentsWithNameStartingWithA() {
        return studentService.getStudentsWithNameStartingWithA();
    }

    @GetMapping("/avg_age_stream")
    public String getAverageAge() {
        return String.format("%.2f", studentService.getAverageAge());
    }

    @GetMapping("/print-parallel")
    public void printStudentsParallel() {
        List<Student> allStudents = (ArrayList<Student>) studentService.getAllStudents();
        System.out.println(allStudents.get(0).getName());
        System.out.println(allStudents.get(1).getName());
        new Thread(() -> {
            System.out.println(allStudents.get(2).getName());
            System.out.println(allStudents.get(3).getName());
        }).start();
        new Thread(() -> {
            System.out.println(allStudents.get(4).getName());
            System.out.println(allStudents.get(5).getName());
        }).start();
    }

    @GetMapping("/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> allStudents = (ArrayList<Student>) studentService.getAllStudents();
        printName(allStudents.get(0).getName());
        printName(allStudents.get(1).getName());
        new Thread(() -> {
            System.out.println(allStudents.get(2).getName());
            System.out.println(allStudents.get(3).getName());
        }).start();
        new Thread(() -> {
            System.out.println(allStudents.get(4).getName());
            System.out.println(allStudents.get(5).getName());
        }).start();
    }

    private synchronized void printName(String studentName) {
        System.out.println(studentName);
    }
}
