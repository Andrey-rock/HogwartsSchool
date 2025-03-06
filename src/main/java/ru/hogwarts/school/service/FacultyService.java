package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty addFaculty(@NotNull Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        logger.info("Was invoked method for get faculty by id");
        return facultyRepository.findById(id).orElse(null);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColour(String colour) {
        logger.info("Was invoked method for get faculties by colour");
        return facultyRepository.findByColourIgnoreCase(colour);
    }

    public Faculty updateFaculty(@NotNull Faculty faculty) {
        logger.info("Was invoked method for update faculty");
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty by id");
        facultyRepository.deleteById(id);
    }

    public Faculty findFacultyByStudentName(String studentName) {
        logger.info("Was invoked method for find faculty by student name");
        return facultyRepository.findFacultyByStudentName(studentName);
    }
}
