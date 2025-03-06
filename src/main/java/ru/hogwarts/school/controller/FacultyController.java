package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable int id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public Collection<Faculty> getFaculties(@RequestParam(required = false) String colour,
                                            @RequestParam(required = false) String studentName) {
        if (!(colour == null)) {
            return facultyService.getFacultiesByColour(colour);
        }
        if (!(studentName == null || studentName.isEmpty())) {
            Collection<Faculty> result = new java.util.ArrayList<>(Collections.emptyList());
            result.add(facultyService.findFacultyByStudentName(studentName));
            return result;
        }
        return facultyService.getAllFaculties();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty facultyUpdated = facultyService.updateFaculty(faculty);
        if (facultyUpdated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyUpdated);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteFaculty(@PathVariable int id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/longer_name")
    public String getLongerNameFaculty() {
        return facultyService.getLongerNameFaculty();
    }
}
