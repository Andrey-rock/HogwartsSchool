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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FacultyRepository FacultyRepository;

    @SpyBean
    FacultyService FacultyService;

    @InjectMocks
    FacultyController FacultyController;
    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    public void getAllFacultiesTest() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1, "Gryffindor", "red"));
        faculties.add(new Faculty(2, "Slytherin", "green"));
        faculties.add(new Faculty(3, "Ravenclaw", "blue"));


        JSONArray facultyObject = new JSONArray();
        facultyObject.add(faculties.get(0));
        facultyObject.add(faculties.get(1));
        facultyObject.add(faculties.get(2));

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//receive
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$.[0].colour").value("red"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Slytherin"))
                .andExpect(jsonPath("$.[1].colour").value("green"))
                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].name").value("Ravenclaw"))
                .andExpect(jsonPath("$.[2].colour").value("blue"));
    }

    @Test
    public void getFacultyByIdTest() throws Exception {
        long id = 1L;
        String name = "Gryffindor";
        String colour = "red";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Faculty faculty = new Faculty(id, name, colour);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id) //send
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));

    }

    @Test
    public void getFacultiesByColourTest() throws Exception {
        long id = 1L;
        String name = "Gryffindor";
        String colour = "red";
        List<Faculty> faculties = new ArrayList<>();

        faculties.add(new Faculty(id, name, colour));

        JSONArray facultyObject = new JSONArray();
        facultyObject.add(faculties.get(0));

        when(facultyRepository.findByColourIgnoreCase(colour)).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?colour=" + colour) //send
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].colour").value(colour));

        Mockito.verify(facultyRepository).findByColourIgnoreCase(colour);
    }

    @Test
    public void saveFacultyTest() throws Exception {
        long id = 1L;
        String name = "Gryffindor";
        String colour = "red";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Faculty faculty = new Faculty(id, name, colour);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty") //send
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));
    }

    @Test
    public void updateFacultyTest() throws Exception {
        long id = 1L;
        String name = "Gryffindor";
        String colour = "red";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Faculty faculty = new Faculty(id, name, colour);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty") //send
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        long id = 1L;

        Mockito.doNothing().when(facultyRepository).deleteById(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)) //send
                .andExpect(status().isNoContent()); //receive
    }
}
