package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();

    private Long nextId = 0L;

    private Long getIdAndIncrement() {
        nextId++;
        return nextId;
    }

    public Student createStudent(String name, int age) {
        Student student = new Student(getIdAndIncrement(), name, age);
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudentById(Long id) {
        return students.get(id);
    }

    public Student updateStudent(Long id, String name, int age) {
        Student student = students.get(id);
        student.setName(name);
        student.setAge(age);
        return student;
    }

    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    public List<Student> getStudentByAge(int age) {
        return students.values().stream()
                .filter(s -> age == s.getAge())
                .collect(Collectors.toList());
    }

    public void deleteAllStudent() {
        students.clear();
    }
}
