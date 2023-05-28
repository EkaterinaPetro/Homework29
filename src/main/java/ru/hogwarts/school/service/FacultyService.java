package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.response.FacultyResponse;
import ru.hogwarts.school.model.response.StudentResponse;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public FacultyResponse createFaculty(String name, String color) {
        Faculty faculty = new Faculty(name, color);
        Faculty facultyCreate = facultyRepository.save(faculty);
        return FacultyMapper.toResponse(facultyCreate);
    }

    public FacultyResponse getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        return FacultyMapper.toResponse(faculty);
    }

    public FacultyResponse updateFaculty(Long id, String name, String color) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        faculty.setName(name);
        faculty.setColor(color);
        facultyRepository.save(faculty);
        return FacultyMapper.toResponse(faculty);
    }

    public FacultyResponse deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        facultyRepository.deleteById(id);
        return FacultyMapper.toResponse(faculty);
    }

    public List<FacultyResponse> getFacultyByColor(String color) {
        List<Faculty> faculties = facultyRepository.findAllByColor(color);
        return faculties.stream()
                .map(FacultyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<FacultyResponse> getFacultyByName(String name) {
        List<Faculty> faculties = facultyRepository.findAllByName(name);
        return faculties.stream()
                .map(FacultyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentsByFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        return faculty.getStudents().stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
