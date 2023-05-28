package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.response.FacultyResponse;
import ru.hogwarts.school.model.response.StudentResponse;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@SpringBootTest
public class StudentServiceTests {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AvatarRepository avatarRepository;

    @BeforeEach
    void clear() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void createStudent_shouldReturnStudent() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        StudentResponse student = studentService.createStudent("Oleg", 24, faculty.getId());
        Assertions.assertEquals("Oleg", student.getName());
        Assertions.assertEquals(24, student.getAge());
    }

    @Test
    void createStudent_shouldCreateStudentUniqueID() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        StudentResponse student1 = studentService.createStudent("Oleg1", 24, faculty.getId());
        StudentResponse student2 = studentService.createStudent("Oleg2", 24, faculty.getId());
        Assertions.assertNotEquals(student1.getId(), student2.getId());
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        StudentResponse student = studentService.createStudent("Oleg", 24, faculty.getId());
        StudentResponse result = studentService.getStudentById(student.getId());
        Assertions.assertEquals("Oleg", result.getName());
        Assertions.assertEquals(24, result.getAge());
    }

    @Test
    void updateStudent_shouldReturnStudentWithNewNameAndAge() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        StudentResponse student = studentService.createStudent("Oleg", 24, faculty.getId());
        StudentResponse result = studentService. updateStudent(student.getId(), "Oleg1", 25);
        Assertions.assertEquals("Oleg1", result.getName());
        Assertions.assertEquals(25, result.getAge());
    }

    @Test
    void updateStudent_shouldThrowStudentNotFoundException() {
        Assertions.assertThrows(StudentNotFoundException.class,
                () -> studentService.updateStudent(12L, "Oleg1", 25));
    }

    @Test
    void deleteStudent_shouldReturnDeletedStudent() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        StudentResponse student = studentService.createStudent("Oleg", 24, faculty.getId());
        StudentResponse result = studentService.deleteStudent(student.getId());
        Assertions.assertEquals("Oleg", result.getName());
        Assertions.assertEquals(24, result.getAge());
    }

    @Test
    void deleteStudent_shouldThrowStudentNotFoundException() {
        Assertions.assertThrows(StudentNotFoundException.class,
                () -> studentService.deleteStudent(12l));
    }

    @Test
    void getStudentByAge_shouldReturnListOfStudents() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        studentService.createStudent("Oleg1", 24, faculty.getId());
        studentService.createStudent("Oleg2", 24, faculty.getId());
        studentService.createStudent("Oleg3", 25, faculty.getId());
        List<StudentResponse> students = studentService.getStudentByAge(24);
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Oleg1", students.get(0).getName());
        Assertions.assertEquals(24, students.get(0).getAge());
        Assertions.assertEquals("Oleg2", students.get(1).getName());
        Assertions.assertEquals(24, students.get(1).getAge());
    }

    @Test
    void getStudentByAgeBetween_shouldReturnListOfStudents() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        studentService.createStudent("Oleg1", 24, faculty.getId());
        studentService.createStudent("Oleg2", 24, faculty.getId());
        studentService.createStudent("Oleg3", 25, faculty.getId());
        List<StudentResponse> students = studentService.getStudentByAgeBetween(20, 24);
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Oleg1", students.get(0).getName());
        Assertions.assertEquals(24, students.get(0).getAge());
        Assertions.assertEquals("Oleg2", students.get(1).getName());
        Assertions.assertEquals(24, students.get(1).getAge());
    }

    @Test
    void getStudentFaculty_shouldReturnFaculty() {
        Faculty faculty = facultyRepository.save(new Faculty("test", "test"));
        StudentResponse student = studentService.createStudent("Oleg1", 24, faculty.getId());
        FacultyResponse result = studentService.getStudentFaculty(student.getId());
        Assertions.assertEquals(faculty.getId(), result.getId());
    }
}
