package ru.hogwarts.school;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.entity.Avatar;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.response.AvatarResponse;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class AvatarServiceTests {
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    @Value("${avatars.directory}")
    private String avatarsDir;

    @BeforeEach
    void clean() throws IOException {
        Path avatarsPath = Path.of(avatarsDir);
        if (Files.exists(avatarsPath)) {
            try (Stream<Path> files = Files.list(avatarsPath)) {
                files.forEach(f -> {
                    try {
                        Files.delete(f);
                    } catch (IOException ignored) {}
                });
            }
            Files.deleteIfExists(avatarsPath);
        }
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private MultipartFile getImage() throws IOException {
        File file = new File("src/test/resources/image.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/jpeg", IOUtils.toByteArray(input));
        return multipartFile;
    }

    @Test
    void uploadAvatar_shouldThrowStudentNotFoundException() {
        Assertions.assertThrows(StudentNotFoundException.class,
                () -> avatarService.uploadAvatar(1L, getImage()));
    }

    @Test
    void uploadAvatar_shouldReturnAvatar() throws IOException {
        Faculty faculty = facultyRepository.save(new Faculty("Slytherin", "Green"));
        Student student = studentRepository.save(new Student("Oleg", 24, faculty));
        AvatarResponse avatar = avatarService.uploadAvatar(student.getId(), getImage());
        List<Avatar> avatars = avatarRepository.findAll();
        Assertions.assertEquals(1, avatars.size());
        Assertions.assertEquals(avatar.getId(), avatars.get(0).getId());
    }

    @Test
    void getAvatar_shouldThrowAvatarNotFoundException() {
        Assertions.assertThrows(AvatarNotFoundException.class,
                () -> avatarService.getAvatar(1L));
    }

    @Test
    void getAvatar_shouldReturnAvatar() throws IOException {
        Faculty faculty = facultyRepository.save(new Faculty("Slytherin", "Green"));
        Student student = studentRepository.save(new Student("Oleg", 24, faculty));
        AvatarResponse avatar = avatarService.uploadAvatar(student.getId(), getImage());
        Avatar result = avatarService.getAvatar(avatar.getId());
        Assertions.assertEquals(avatar.getId(), result.getId());
        Assertions.assertEquals(student.getId(), result.getStudent().getId());
    }
}
