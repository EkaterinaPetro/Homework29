package ru.hogwarts.school;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacultyControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyService facultyService;
    @InjectMocks
    private FacultyController facultyController;

    @Test
    void createFaculty_ShouldReturnFaculty() throws Exception {
        Long id = 2L;
        String name = "Faculty";
        String color = "Green";

        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name);
        requestObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFaculty_ShouldReturnFaculty() throws Exception {
        Long id = 2L;
        String name = "Faculty";
        String color = "Green";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void updateFaculty_ShouldReturnFaculty() throws Exception {
        Long id = 2L;
        String name = "Faculty";
        String name2 = "Facultyty";
        String color = "Green";
        String color2 = "Blue";

        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name2);
        requestObject.put("color", color2);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty.setId(id);
        faculty.setName(name2);
        faculty.setColor(color2);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + id)
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.color").value(color2));
    }

    @Test
    void deleteFaculty_ShouldReturnFaculty() throws Exception {
        Long id = 2L;
        String name = "Faculty";
        String color = "Green";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFacultyByColor_ShouldReturnListOfFaculties() throws Exception {
        Long id = 2L;
        Long id2 = 1L;
        String name = "Faculty";
        String name2 = "Facultyty";
        String color = "Green";
        String color2 = "Green";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        when(facultyRepository.findAllByColor(color)).thenReturn(List.of(faculty, faculty2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color?color=" + color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].color").value(color))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andExpect(jsonPath("$.[1].color").value(color2));
    }

    @Test
    void getFacultyByName_ShouldReturnListOfFaculties() throws Exception {
        Long id = 2L;
        Long id2 = 1L;
        String name = "Faculty";
        String name2 = "Faculty";
        String color = "Green";
        String color2 = "Blue";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        when(facultyRepository.findAllByName(name)).thenReturn(List.of(faculty, faculty2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/name?name=" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].color").value(color))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andExpect(jsonPath("$.[1].color").value(color2));
    }

    @Test
    void getStudentsByFaculty_ShouldReturnListOfStudents() throws Exception {
        Long id = 1L;
        Long id2 = 2L;
        String name = "Bob";
        String name2 = "Jenna";
        int age = 17;
        int age2 = 19;
        Long facultyId = 2L;
        String facultyName = "Faculty";
        String facultyColor = "Green";

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(facultyColor);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);
        student2.setFaculty(faculty);

        faculty.setStudents(List.of(student, student2));

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].age").value(age))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andExpect(jsonPath("$.[1].age").value(age2));
    }
}
