package ru.hogwarts.school.mapper;

import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.response.StudentResponse;

public class StudentMapper {
    public static StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setAge(student.getAge());
        return response;
    }
}
