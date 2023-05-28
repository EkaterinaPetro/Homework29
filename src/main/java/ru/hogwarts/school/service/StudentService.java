package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(String name, int age, Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        Student student = new Student(name, age, faculty);
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

    public List<Student> getStudentByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        return student.getFaculty();
    }
}
