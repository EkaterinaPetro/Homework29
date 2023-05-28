package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@SpringBootTest
public class FacultyServiceTests {
    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void clear() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void createFaculty_shouldReturnFaculty() {
        Faculty faculty = facultyService.createFaculty("Slytherin", "Green");
        Assertions.assertEquals("Slytherin", faculty.getName());
        Assertions.assertEquals("Green", faculty.getColor());
    }

    @Test
    void createFaculty_shouldCreateFacultyUniqueID() {
        Faculty faculty1 = facultyService.createFaculty("Slytherin", "Green");
        Faculty faculty2 = facultyService.createFaculty("Hufflepuff ", "Yellow");
        Assertions.assertNotEquals(faculty1.getId(), faculty2.getId());
    }

    @Test
    void getFacultyById_shouldReturnFaculty() {
        Faculty faculty = facultyService.createFaculty("Slytherin", "Green");
        Faculty result = facultyService.getFacultyById(faculty.getId());
        Assertions.assertEquals("Slytherin", result.getName());
        Assertions.assertEquals("Green", result.getColor());
    }

    @Test
    void updateFaculty_shouldReturnFacultyWithNewNameAndColor() {
        Faculty faculty = facultyService.createFaculty("Slytherin", "Green");
        Faculty result = facultyService.updateFaculty(faculty.getId(), "Slytherin1", "Green2");
        Assertions.assertEquals("Slytherin1", result.getName());
        Assertions.assertEquals("Green2", result.getColor());
    }

    @Test
    void deleteFaculty_shouldReturnDeletedFaculty() {
        Faculty faculty = facultyService.createFaculty("Slytherin", "Green");
        Faculty result = facultyService.deleteFaculty(faculty.getId());
        Assertions.assertEquals("Slytherin", result.getName());
        Assertions.assertEquals("Green", result.getColor());
    }

    @Test
    void getFacultyByColor_shouldReturnListOfFaculties() {
        facultyService.createFaculty("Slytherin", "Green");
        facultyService.createFaculty("Hufflepuff ", "Yellow");
        facultyService.createFaculty("Slytherin2", "Green");
        List<Faculty> faculties = facultyService.getFacultyByColor("Green");
        Assertions.assertEquals(2, faculties.size());
        Assertions.assertEquals("Slytherin", faculties.get(0).getName());
        Assertions.assertEquals("Green", faculties.get(0).getColor());
        Assertions.assertEquals("Slytherin2", faculties.get(1).getName());
        Assertions.assertEquals("Green", faculties.get(1).getColor());
    }

    @Test
    void getFacultyByName_shouldReturnListOfFaculties() {
        facultyService.createFaculty("Slytherin", "Green");
        facultyService.createFaculty("Hufflepuff ", "Yellow");
        facultyService.createFaculty("Slytherin2", "Green");
        List<Faculty> faculties = facultyService.getFacultyByName("Slytherin");
        Assertions.assertEquals(1, faculties.size());
        Assertions.assertEquals("Slytherin", faculties.get(0).getName());
        Assertions.assertEquals("Green", faculties.get(0).getColor());
    }

    @Test
    void getStudentsByFaculty_shouldReturnListOfStudents() {
        Faculty faculty = facultyService.createFaculty("Slytherin", "Green");
        studentService.createStudent("Oleg1", 24, faculty.getId());
        studentService.createStudent("Oleg2", 24, faculty.getId());
        List<Student> students = facultyService.getStudentsByFaculty(faculty.getId());
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Oleg1", students.get(0).getName());
        Assertions.assertEquals(24, students.get(0).getAge());
        Assertions.assertEquals("Oleg2", students.get(1).getName());
        Assertions.assertEquals(24, students.get(1).getAge());
    }
}
