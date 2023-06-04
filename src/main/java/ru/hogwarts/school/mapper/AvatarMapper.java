package ru.hogwarts.school.mapper;

import ru.hogwarts.school.model.entity.Avatar;
import ru.hogwarts.school.model.response.AvatarResponse;

public class AvatarMapper {
    public static AvatarResponse toResponse(Avatar avatar) {
        AvatarResponse avatarResponse = new AvatarResponse();
        avatarResponse.setId(avatar.getId());
        avatarResponse.setFilePath(avatar.getFilePath());
        avatarResponse.setFileSize(avatar.getFileSize());
        avatarResponse.setMediaType(avatar.getMediaType());
        return avatarResponse;
    }
}
