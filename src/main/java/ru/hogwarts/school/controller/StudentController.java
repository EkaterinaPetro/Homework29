package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.request.StudentCreateRequest;
import ru.hogwarts.school.model.request.StudentUpdateRequest;
import ru.hogwarts.school.model.response.FacultyResponse;
import ru.hogwarts.school.model.response.StudentResponse;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentCreateRequest request) {
        try {
            return ResponseEntity.ok(studentService.createStudent(request.getName(), request.getAge(),
                    request.getFacultyId()));
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudentById(id));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id,
                                                         @RequestBody StudentUpdateRequest request) {
        try {
            return ResponseEntity.ok(studentService.updateStudent(id, request.getName(), request.getAge()));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponse> deleteStudent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.deleteStudent(id));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/age")
    public List<StudentResponse> getStudentByAge(@RequestParam int age) {
        return studentService.getStudentByAge(age);
    }

    @GetMapping("/age-between")
    public List<StudentResponse> getStudentByAgeBetween(@RequestParam int max, @RequestParam int min) {
        return studentService.getStudentByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<FacultyResponse> getStudentFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudentFaculty(id));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    public Long countStudents() {
        return studentService.countStudents();
    }

    @GetMapping("/average-age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/last-5")
    public List<StudentResponse> getLast5Students() {
        return studentService.getLast5Students();
    }
}
