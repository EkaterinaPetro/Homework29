package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(String name, String color) {
        Faculty faculty = new Faculty(name, color);
        Faculty facultyCreate = facultyRepository.save(faculty);
        return facultyCreate;
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(Long id, String name, String color) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        faculty.setName(name);
        faculty.setColor(color);
        facultyRepository.save(faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        facultyRepository.deleteById(id);
        return faculty;
    }

    public List<Faculty> getFacultyByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> getFacultyByName(String name) {
        return facultyRepository.findAllByName(name);
    }

    public List<Student> getStudentsByFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        return faculty.getStudents();
    }
}