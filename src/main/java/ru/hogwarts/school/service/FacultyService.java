package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Stream;

@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public FacultyResponse createFaculty(String name, String color) {
        logger.info("Was invoked method for create faculty");
        Faculty faculty = new Faculty(name, color);
        Faculty facultyCreate = facultyRepository.save(faculty);
        return FacultyMapper.toResponse(facultyCreate);
    }

    public FacultyResponse getFacultyById(Long id) {
        logger.info("Was invoked method for get faculty by id");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new FacultyNotFoundException();
        });
        return FacultyMapper.toResponse(faculty);
    }

    public FacultyResponse updateFaculty(Long id, String name, String color) {
        logger.info("Was invoked method for update faculty");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new FacultyNotFoundException();
        });
        faculty.setName(name);
        faculty.setColor(color);
        facultyRepository.save(faculty);
        return FacultyMapper.toResponse(faculty);
    }

    public FacultyResponse deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new FacultyNotFoundException();
        });
        facultyRepository.deleteById(id);
        return FacultyMapper.toResponse(faculty);
    }

    public List<FacultyResponse> getFacultyByColor(String color) {
        logger.info("Was invoked method for get faculty by color");
        List<Faculty> faculties = facultyRepository.findAllByColor(color);
        return faculties.stream()
                .map(FacultyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<FacultyResponse> getFacultyByName(String name) {
        logger.info("Was invoked method for get faculty by name");
        List<Faculty> faculties = facultyRepository.findAllByName(name);
        return faculties.stream()
                .map(FacultyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentsByFaculty(Long id) {
        logger.info("Was invoked method for get students by faculty");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new FacultyNotFoundException();
        });
        return faculty.getStudents().stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public String getLongestName() {
        logger.info("Was invoked method for get longest name");
        return facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max((s1, s2) -> {
                    if (s1.length() == s2.length()) {
                        return 0;
                    }
                    if (s1.length() > s2.length()) {
                        return 1;
                    }
                    return -1;
                }).orElseThrow(FacultyNotFoundException::new);
    }

    public Long getSumFrom1To1000000() {
        /*
        Это еще быстрее, но тут нет стримов

        Long n = 1000000L;
        return  (1 + n)/2 * n;
         */

        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .mapToLong(Integer::longValue)
                .sum();
    }
}
