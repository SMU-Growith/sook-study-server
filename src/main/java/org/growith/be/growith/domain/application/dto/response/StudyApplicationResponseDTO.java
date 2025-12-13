package org.growith.be.growith.domain.application.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.application.entity.ApplicationStatus;
import org.growith.be.growith.domain.study.entity.enums.StudyFormat;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.entity.enums.StudyStyleCategory;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

import java.time.LocalDateTime;
import java.util.List;

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

    @Builder
    public record MyApplicationCardDTO(
            Long applicationId,
            Long studyId,
            String title,
            StudyStatus studyStatus,
            String studyFormat,
            String studyFieldName,
            String studyStyleCategory,
            String nickname,
            Long scrapCount,
            Boolean isScraped,
            LocalDateTime createdAt,
            ApplicationStatus applicationStatus
    ){}
}

