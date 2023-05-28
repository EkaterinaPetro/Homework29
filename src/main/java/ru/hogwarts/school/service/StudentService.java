package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentRepository.save(student);
        return student;
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, String name, int age) {
        Student student = studentRepository.findById(id).orElse(null);
        student.setName(name);
        student.setAge(age);
        studentRepository.save(student);
        return student;
    }

    public Student deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        studentRepository.deleteById(id);
        return student;
    }

    public List<Student> getStudentByAge(int age) {
        return studentRepository.findAllByAge(age);
    }
}
