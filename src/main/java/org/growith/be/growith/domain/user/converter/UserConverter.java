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
                .noticeYn(Boolean.TRUE.equals(user.getIsNotice()))
                // TODO: studyStyle 구현 필요
                .studyStyle("하드코딩")
                .personalityType(org.growith.be.growith.domain.personality.converter.PersonalityConverter.toUserPersonalityTypeDto(user.getPersonalityResultType()))
                .build();
    }

    // User -> UserResponseDTO.ChangeInfo
    public static UserResponseDTO.ChangeInfo toChangeInfo(User user){
        return UserResponseDTO.ChangeInfo.builder()
                .nickName(user.getNickName())
                .studentStatus(user.getStudentStatus())
                .major(user.getMajor())
                .phoneNumber(user.getPhoneNumber())
                .noticeYn(Boolean.TRUE.equals(user.getIsNotice()))
                .build();
    }
}
