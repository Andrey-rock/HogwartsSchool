package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {

    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty addFaculty(@NotNull Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    public Faculty getFaculty(long id) {
        return faculties.get(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    public Collection<Faculty> getFacultiesByColour(String colour) {
        Collection<Faculty> facultiesByColour = getAllFaculties();
        return facultiesByColour.stream()
                .filter(f -> f.getColour().equals(colour))
                .toList();
    }

    public Faculty updateFaculty(@NotNull Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }
}
