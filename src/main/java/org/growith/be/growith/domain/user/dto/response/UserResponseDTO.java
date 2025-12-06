package org.growith.be.growith.domain.user.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

public record UserResponseDTO() {

    @Builder
    public record ChangeInfo(
            String nickName,
            StudentStatus studentStatus,
            Major major,
            String phoneNumber
//            String studyStyle
    ){}

    @Builder
    public record UserProfileDTO(
        String nickName,
        StudentStatus studentStatus,
        Major major,
        String studyStyle
    ){}
}
