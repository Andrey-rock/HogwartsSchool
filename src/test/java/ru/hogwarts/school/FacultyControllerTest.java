package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyController facultyController;

    @AfterEach
    public void tearDown() {
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoadsTest() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void getAllFacultiesTest() {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(createTestFaculty("Griffindor", "red"));
        faculties.add(createTestFaculty("Slytherin", "green"));

        ResponseEntity<List<Faculty>> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Faculty> facultiesResp = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        Assertions.assertThat(facultiesResp).isEqualTo(faculties);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getFacultyByIdTest() {
        Faculty faculty = createTestFaculty("Griffindor", "red");
        long id = faculty.getId();

        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty/" + id,
                HttpMethod.GET, null, Faculty.class);

        Faculty facultyResp = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        Assertions.assertThat(facultyResp).isEqualTo(faculty);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getFacultyByColourTest() {
        String colour = "red";
        Faculty faculty = createTestFaculty("Griffindor", "red");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange("http://localhost:" + port +
                        "/faculty?colour=" + colour,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<Faculty> facultyResp = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        Assertions.assertThat(facultyResp).isEqualTo(faculties);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Griffindor");
        faculty.setColour("red");
        HttpEntity<Faculty> request = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.POST, request, Faculty.class);

        HttpStatusCode status = response.getStatusCode();
        Faculty facultyResp = response.getBody();
        long id = response.getBody().getId();

        Assertions.assertThat(status).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(facultyResp).isEqualTo(new Faculty(id, "Griffindor", "red"));
    }

    @Test
    void updateFacultyTest() {
        Faculty faculty1 = createTestFaculty("Griffindor", "red");
        long id = faculty1.getId();
        Faculty faculty2 = new Faculty();
        faculty2.setId(id);
        faculty2.setName("Griffindor");
        faculty2.setColour("gold");
        HttpEntity<Faculty> request = new HttpEntity<>(faculty2);

        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, request, Faculty.class);

        HttpStatusCode status = response.getStatusCode();
        Faculty facultyResp = response.getBody();

        Assertions.assertThat(facultyResp).isEqualTo(faculty2);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }


    @Test
    void deleteFacultyTest() {
        Faculty faculty = createTestFaculty("Griffindor", "red");
        long id = faculty.getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/faculty/" + id,
                HttpMethod.DELETE, null, String.class);

        HttpStatusCode status = response.getStatusCode();
        Assertions.assertThat(status).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(facultyRepository.count()).isEqualTo(0);
    }

    private Faculty createTestFaculty(String name, String colour) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColour(colour);
        return facultyRepository.save(faculty);
    }
}
