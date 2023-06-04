package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.request.FacultyCreateRequest;
import ru.hogwarts.school.model.request.FacultyUpdateRequest;
import ru.hogwarts.school.model.response.FacultyResponse;
import ru.hogwarts.school.model.response.StudentResponse;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public FacultyResponse createFaculty(@RequestBody FacultyCreateRequest request) {
        return facultyService.createFaculty(request.getName(), request.getColor());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponse> getFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.getFacultyById(id));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponse> updateFaculty(@PathVariable Long id,
                                                         @RequestBody FacultyUpdateRequest request) {
        try {
            return ResponseEntity.ok(facultyService.updateFaculty(id, request.getName(), request.getColor()));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FacultyResponse> deleteFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.deleteFaculty(id));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/color")
    public List<FacultyResponse> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping("/name")
    public List<FacultyResponse> getFacultyByName(@RequestParam String name) {
        return facultyService.getFacultyByName(name);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.getStudentsByFaculty(id));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/longest-name")
    public String getLongestName() {
        return facultyService.getLongestName();
    }

    @GetMapping("/sum")
    public Long getSumFrom1To1000000() {
        return facultyService.getSumFrom1To1000000();
    }

}
