package ru.hogwarts.school;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;

    @Test
    void createStudent_ShouldReturnStudent() throws Exception {
        Long id = 1L;
        String name = "Bob";
        int age = 17;
        Long facultyId = 2L;
        String facultyName = "Faculty";
        String facultyColor = "Green";

        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name);
        requestObject.put("age", age);
        requestObject.put("facultyId", facultyId);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(facultyColor);
        
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void getStudent_ShouldReturnStudent() throws Exception {
        Long id = 1L;
        String name = "Bob";
        int age = 17;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void updateStudent_ShouldReturnStudent() throws Exception {
        Long id = 1L;
        String name = "Bob";
        String name2 = "Bobinsky";
        int age = 17;
        int age2 = 19;

        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name2);
        requestObject.put("age", age2);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + id)
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.age").value(age2));
    }

    @Test
    void deleteStudent_ShouldReturnStudent() throws Exception {
        Long id = 1L;
        String name = "Bob";
        int age = 17;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void getStudentByAge_ShouldReturnListOfStudents() throws Exception {
        Long id = 1L;
        Long id2 = 2L;
        String name = "Bob";
        String name2 = "Jenna";
        int age = 17;
        int age2 = 17;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentRepository.findAllByAge(age)).thenReturn(List.of(student, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age?age=" + age)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].age").value(age))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andExpect(jsonPath("$.[1].age").value(age2));
    }

    @Test
    void getStudentByAgeBetween_ShouldReturnListOfStudents() throws Exception {
        Long id = 1L;
        Long id2 = 2L;
        String name = "Bob";
        String name2 = "Jenna";
        int age = 17;
        int age2 = 19;
        int min = 15;
        int max = 20;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentRepository.findByAgeBetween(min, max)).thenReturn(List.of(student, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-between?min=" + min + "&max=" + max)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].age").value(age))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andExpect(jsonPath("$.[1].age").value(age2));
    }

    @Test
    void getStudentFaculty_shouldReturnFaculty() throws Exception {
        Long id = 1L;
        String name = "Bob";
        int age = 17;
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

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id + "/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }

    @Test
    void countStudents_shouldReturnNumber() throws Exception {
        when(studentRepository.count()).thenReturn(2L);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void getAverageAge_shouldReturnNumber() throws Exception {
        when(studentRepository.getAverageAge()).thenReturn(23.3);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/average-age")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("23.3"));
    }

    @Test
    void getLast5Students_shouldReturnListOfStudents() throws Exception {
        Long id = 1L;
        Long id2 = 2L;
        String name = "Bob";
        String name2 = "Jenna";
        int age = 17;
        int age2 = 19;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student2);

        when(studentRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(students));

        mockMvc. perform(MockMvcRequestBuilders
                        .get("/student/last-5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].age").value(age))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andExpect(jsonPath("$.[1].age").value(age2));
    }

    @Test
    void getAllNamesStartWithA_shouldReturnListOfNames() throws Exception {
        Long id = 1L;
        Long id2 = 2L;
        String name = "Andrea";
        String name2 = "Amber";
        int age = 17;
        int age2 = 19;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentRepository.findAll()).thenReturn(List.of(student2, student));

        mockMvc. perform(MockMvcRequestBuilders
                        .get("/student/name-star-A")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value("AMBER"))
                .andExpect(jsonPath("$.[1]").value("ANDREA"));
    }

    @Test
    void getAverageAge2_shouldReturnNumber() throws Exception {
        Long id = 1L;
        Long id2 = 2L;
        String name = "Andrea";
        String name2 = "Amber";
        int age = 20;
        int age2 = 20;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentRepository.findAll()).thenReturn(List.of(student, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/average-age2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("20.0"));
    }
}
