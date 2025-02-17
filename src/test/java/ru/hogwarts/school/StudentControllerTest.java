package ru.hogwarts.school;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    StudentController studentController;

    @Test
    public void getAllStudentsTest() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Garry", 11));
        students.add(new Student(2, "Germiona", 12));
        students.add(new Student(3, "Ron", 13));

        JSONArray studentObject = new JSONArray();
        studentObject.add(students.get(0));
        studentObject.add(students.get(1));
        studentObject.add(students.get(2));

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//receive
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Garry"))
                .andExpect(jsonPath("$.[0].age").value(11))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Germiona"))
                .andExpect(jsonPath("$.[1].age").value(12))
                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].name").value("Ron"))
                .andExpect(jsonPath("$.[2].age").value(13));
    }

    @Test
    public void getStudentByIdTest() throws Exception {
        long id = 1L;
        String name = "Garry";
        int age = 11;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Student student = new Student(id, name, age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id) //send
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

    }

    @Test
    public void getStudentsBetweenMinMaxAgeTest() throws Exception {
        int minAge = 12;
        int maxAge = 13;

        List<Student> students = new ArrayList<>();

        students.add(new Student(2, "Germiona", 12));
        students.add(new Student(3, "Ron", 13));

        JSONArray studentObject = new JSONArray();
        studentObject.add(students.get(0));
        studentObject.add(students.get(1));

        when(studentRepository.findByAgeBetween(minAge, maxAge)).thenReturn(students);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/minmax?min=" + minAge + "&max=" + maxAge)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//receive
                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].name").value("Germiona"))
                .andExpect(jsonPath("$.[0].age").value(12))
                .andExpect(jsonPath("$.[1].id").value(3))
                .andExpect(jsonPath("$.[1].name").value("Ron"))
                .andExpect(jsonPath("$.[1].age").value(13));

        Mockito.verify(studentRepository).findByAgeBetween(minAge, maxAge);
    }

    @Test
    public void saveStudentTest() throws Exception {
        long id = 1L;
        String name = "Garry";
        int age = 11;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Student student = new Student(id, name, age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student") //send
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void updateStudentTest() throws Exception {
        long id = 1L;
        String name = "Garry";
        int age = 11;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Student student = new Student(id, name, age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student") //send
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        long id = 1L;

        Mockito.doNothing().when(studentRepository).deleteById(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)) //send
                .andExpect(status().isNoContent()); //receive
    }
}
