package org.growith.be.growith.domain.user.converter;

import org.growith.be.growith.domain.user.dto.response.UserResponseDTO;
import org.growith.be.growith.domain.user.entity.User;

public class UserConverter {

    // User -> UserResponseDTO.UserProfileDTO
    public static UserResponseDTO.UserProfileDTO toUserProfileDTO(User user){
        return UserResponseDTO.UserProfileDTO.builder()
                .nickName(user.getNickName())
                .studentStatus(user.getStudentStatus())
                .major(user.getMajor())
                .phoneNumber(user.getPhoneNumber())
                // TODO: studyStyle 구현 필요
                .studyStyle(null)
                .build();
    }

    // User -> UserResponseDTO.ChangeInfo
    public static UserResponseDTO.ChangeInfo toChangeInfo(User user){
        return UserResponseDTO.ChangeInfo.builder()
                .nickName(user.getNickName())
                .studentStatus(user.getStudentStatus())
                .major(user.getMajor())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
