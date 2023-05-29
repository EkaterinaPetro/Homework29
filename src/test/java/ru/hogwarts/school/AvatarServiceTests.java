package ru.hogwarts.school;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.entity.Avatar;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.response.AvatarResponse;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTests {
    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private StudentRepository studentRepository;
    private AvatarService avatarService;

    @BeforeEach
    void setup() {
        avatarService = new AvatarService(avatarRepository, studentRepository);
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
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(StudentNotFoundException.class,
                () -> avatarService.uploadAvatar(id, getImage()));
    }

    @Test
    void uploadAvatar_shouldReturnAvatar() throws IOException {
        Long id = 1L;
        Long avatarId = 2L;
        String name = "Bob";
        int age = 17;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Avatar avatar = new Avatar();
        avatar.setId(avatarId);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(avatarRepository.findAvatarByStudentId(id)).thenReturn(Optional.empty());
        when(avatarRepository.save(any(Avatar.class))).thenReturn(avatar);

        AvatarResponse result = avatarService.uploadAvatar(student.getId(), getImage());
        Assertions.assertEquals(avatarId, result.getId());
    }

    @Test
    void getAvatar_shouldThrowAvatarNotFoundException() {
        Long id = 1L;

        when(avatarRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(AvatarNotFoundException.class,
                () -> avatarService.getAvatar(id));
    }

    @Test
    void getAvatar_shouldReturnAvatar() {
        Long id = 2L;

        Avatar avatar = new Avatar();
        avatar.setId(id);

        when(avatarRepository.findById(id)).thenReturn(Optional.of(avatar));

        Avatar result = avatarService.getAvatar(avatar.getId());
        Assertions.assertEquals(avatar.getId(), result.getId());
    }

    @Test
    void getAll_shouldReturnListOfAvatars() {
        Long id = 2L;
        Long id2 = 1L;
        int page = 0;
        int size = 2;

        Avatar avatar = new Avatar();
        avatar.setId(id);

        Avatar avatar2 = new Avatar();
        avatar2.setId(id2);

        List<Avatar> avatars = new ArrayList<>();
        avatars.add(avatar);
        avatars.add(avatar2);


        when(avatarRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(avatars));
        List<AvatarResponse> result = avatarService.getAll(page, size);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(id, result.get(0).getId());
        Assertions.assertEquals(id2, result.get(1).getId());
    }
}
