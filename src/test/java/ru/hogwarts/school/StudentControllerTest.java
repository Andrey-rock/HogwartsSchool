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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void getAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(createTestStudent("Garry", 11));
        students.add(createTestStudent("Ron", 12));

        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Student> studentsResp = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        Assertions.assertThat(studentsResp).isEqualTo(students);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getStudentById() {
        Student student = createTestStudent("Garry", 11);
        long id = student.getId();

        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/student/" + id,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        Student studentsResp = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        Assertions.assertThat(studentsResp).isEqualTo(student);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getStudentsByAgeMinMax() {
        int min = 12;
        int max = 13;
        List<Student> students = new ArrayList<>();
        createTestStudent("Garry", 11);
        students.add(createTestStudent("Ron", 12));
        students.add(createTestStudent("Germiona", 13));

        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port +
                        "/student/minmax?min=" + min + "&max=" + max,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Student> studentsResp = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        Assertions.assertThat(studentsResp).isEqualTo(students);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void tesCreateStudent() {
        Student student = new Student();
        student.setName("Garry");
        student.setAge(11);
        HttpEntity<Student> request = new HttpEntity<>(student);

        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.POST, request, Student.class);

        HttpStatusCode status = response.getStatusCode();
        Student facultyResp = response.getBody();
        long id = response.getBody().getId();

        Assertions.assertThat(status).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(facultyResp).isEqualTo(new Student(id, "Garry", 11));
    }

    @Test
    void testPutStudent() {
        Student student1 = createTestStudent("Garry", 11);
        long id = student1.getId();
        Student student2 = new Student();
        student2.setId(id);
        student2.setName("Garry");
        student2.setAge(12);

        HttpEntity<Student> request = new HttpEntity<>(student2);

        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT, request, Student.class);

        HttpStatusCode status = response.getStatusCode();
        Student facultyResp = response.getBody();

        Assertions.assertThat(facultyResp).isEqualTo(student2);
        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testDeleteStudent() {
        Student student = createTestStudent("Garry", 11);
        long id = student.getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/student/" + id,
                HttpMethod.DELETE, null, String.class);

        HttpStatusCode status = response.getStatusCode();
        Assertions.assertThat(status).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(studentRepository.count()).isEqualTo(0);
    }


    private Student createTestStudent(String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        return studentRepository.save(student);
    }
}
