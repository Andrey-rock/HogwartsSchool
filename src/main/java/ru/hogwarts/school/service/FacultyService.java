package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(@NotNull Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColour(String colour) {
        Collection<Faculty> facultiesByColour = getAllFaculties();
        return facultiesByColour.stream()
                .filter(f -> f.getColour().equals(colour))
                .toList();
    }

    public Faculty updateFaculty(@NotNull Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }
}
