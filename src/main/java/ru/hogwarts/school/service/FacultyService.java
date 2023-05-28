package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();

    private Long nextId = 0L;

    private Long getIdAndIncrement() {
        nextId++;
        return nextId;
    }

    public Faculty createFaculty(String name, String color) {
        Faculty student = new Faculty(getIdAndIncrement(), name, color);
        faculties.put(student.getId(), student);
        return student;
    }

    public Faculty getFacultyById(Long id) {
        return faculties.get(id);
    }

    public Faculty updateFaculty(Long id, String name, String color) {
        Faculty student = faculties.get(id);
        student.setName(name);
        student.setColor(color);
        return student;
    }

    public Faculty deleteFaculty(Long id) {
        return faculties.remove(id);
    }

    public List<Faculty> getFacultyByColor(String color) {
        return faculties.values().stream()
                .filter(f -> color.equals(f.getColor()))
                .collect(Collectors.toList());
    }

    public void deleteAllFaculties() {
        faculties.clear();
    }
}
