package org.growith.be.growith.domain.application.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.application.entity.ApplicationStatus;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

public record StudyApplicationResponseDTO() {

    @Builder
    public record StudyApplicationDetailDTO(
            Long applicationId,
            Long studyId,
            Long userId,
            String nickName,
            StudentStatus studentStatus,
            String major,
            String phoneNumber,
            String motivation,
            ApplicationStatus applicationStatus
    ){}

    @Builder
    public record UpdateApplicationResponseDTO(
            Long applicationId,
            Long studyId,
            ApplicationStatus applicationStatus
    ){}
}
