package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.request.FacultyCreateRequest;
import ru.hogwarts.school.model.request.FacultyUpdateRequest;
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
    public Faculty createFaculty(@RequestBody FacultyCreateRequest request) {
        return facultyService.createFaculty(request.getName(), request.getColor());
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody FacultyUpdateRequest request) {
        try {
            return ResponseEntity.ok(facultyService.updateFaculty(id, request.getName(), request.getColor()));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.deleteFaculty(id));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/color")
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping("/name")
    public List<Faculty> getFacultyByName(@RequestParam String name) {
        return facultyService.getFacultyByName(name);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudentsByFaculty(@PathVariable Long id) {
        return facultyService.getStudentsByFaculty(id);
    }
}
