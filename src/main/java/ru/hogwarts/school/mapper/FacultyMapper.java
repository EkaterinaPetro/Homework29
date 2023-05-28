package ru.hogwarts.school.mapper;

import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.response.FacultyResponse;

public class FacultyMapper {
    public static FacultyResponse toResponse(Faculty faculty) {
        FacultyResponse response = new FacultyResponse();
        response.setId(faculty.getId());
        response.setName(faculty.getName());
        response.setColor(faculty.getColor());
        return response;
    }
}
