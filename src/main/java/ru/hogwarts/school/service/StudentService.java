package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.response.FacultyResponse;
import ru.hogwarts.school.model.response.StudentResponse;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class
StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public StudentResponse createStudent(String name, int age, Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(FacultyNotFoundException::new);
        Student student = new Student(name, age, faculty);
        Student studentCreate = studentRepository.save(student);
        return StudentMapper.toResponse(studentCreate);
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        return StudentMapper.toResponse(student);
    }

    public StudentResponse updateStudent(Long id, String name, int age) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        student.setName(name);
        student.setAge(age);
        studentRepository.save(student);
        return StudentMapper.toResponse(student);
    }

    public StudentResponse deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.deleteById(id);
        return StudentMapper.toResponse(student);
    }

    public List<StudentResponse> getStudentByAge(int age) {
        List<Student> students = studentRepository.findAllByAge(age);
        return students.stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentByAgeBetween(int min, int max) {
        List<Student> students = studentRepository.findByAgeBetween(min, max);
        return students.stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public FacultyResponse getStudentFaculty(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        return FacultyMapper.toResponse(student.getFaculty());
    }
}
