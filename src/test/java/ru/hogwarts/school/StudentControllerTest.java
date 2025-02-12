package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void getAllStudents() throws Exception {
        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isEqualTo("[{\"id\":1,\"name\":\"Garry\",\"age\":11}," +
                        "{\"id\":2,\"name\":\"Germiona\",\"age\":12}," +
                        "{\"id\":3,\"name\":\"Ron\",\"age\":13}]");
    }

    @Test
    void getStudentById() throws Exception {
        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/1", String.class))
                .isEqualTo("{\"id\":1,\"name\":\"Garry\",\"age\":11}");
    }

    @Test
    void getStudentsByAgeMinMax() throws Exception {
        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/minmax?min=12&max=13", String.class))
                .isEqualTo("[{\"id\":2,\"name\":\"Germiona\",\"age\":12}," +
                        "{\"id\":3,\"name\":\"Ron\",\"age\":13}]");
    }

    @Test
    void testPostStudent() throws Exception {

        Student student = new Student();
        student.setName("Test");
        student.setAge(10);

        Assertions
                .assertThat(restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isEqualTo("{\"id\":1,\"name\":\"Test\",\"age\":10}");
    }

    @Test
    void testPutStudent() throws Exception {
        Student student = new Student();
        student.setId(3);
        student.setName("Ron");
        student.setAge(12);


    }
}
