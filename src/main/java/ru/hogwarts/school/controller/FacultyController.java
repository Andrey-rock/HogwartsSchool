package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

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
    public Collection<Faculty> getAllFaculties(@RequestParam(required = false) String colour) {
        if (colour == null) {
            return facultyService.getAllFaculties();
        }
        return facultyService.getFacultiesByColour(colour);
    }

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

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable int id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
}
