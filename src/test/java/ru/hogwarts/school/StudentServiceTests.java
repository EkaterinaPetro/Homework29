package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@SpringBootTest
public class StudentServiceTests {

    @Autowired
    private StudentService studentService;

    @BeforeEach
    void clear() {
        studentService.deleteAllStudent();
    }

    @Test
    void createStudent_shouldReturnStudent() {
        Student student = studentService.createStudent("Oleg", 24);
        Assertions.assertEquals("Oleg", student.getName());
        Assertions.assertEquals(24, student.getAge());
    }

    @Test
    void createStudent_shouldCreateStudentUniqueID() {
        Student student1 = studentService.createStudent("Oleg1", 24);
        Student student2 = studentService.createStudent("Oleg2", 24);
        Assertions.assertNotEquals(student1.getId(), student2.getId());
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Student student = studentService.createStudent("Oleg", 24);
        Student result = studentService.getStudentById(student.getId());
        Assertions.assertEquals("Oleg", result.getName());
        Assertions.assertEquals(24, result.getAge());
    }

    @Test
    void updateStudent_shouldReturnStudentWithNewNameAndAge() {
        Student student = studentService.createStudent("Oleg", 24);
        Student result = studentService. updateStudent(student.getId(), "Oleg1", 25);
        Assertions.assertEquals("Oleg1", result.getName());
        Assertions.assertEquals(25, result.getAge());
    }

    @Test
    void deleteStudent_shouldReturnDeletedStudent() {
        studentService.createStudent("Oleg", 24);
        Student student = studentService.deleteStudent(1L);
        Assertions.assertEquals("Oleg", student.getName());
        Assertions.assertEquals(24, student.getAge());
    }

    @Test
    void getStudentByAge_shouldReturnListOfStudents() {
        studentService.createStudent("Oleg1", 24);
        studentService.createStudent("Oleg2", 24);
        studentService.createStudent("Oleg3", 25);
        List<Student> students = studentService.getStudentByAge(24);
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Oleg1", students.get(0).getName());
        Assertions.assertEquals(24, students.get(0).getAge());
        Assertions.assertEquals("Oleg2", students.get(1).getName());
        Assertions.assertEquals(24, students.get(1).getAge());
    }
}
