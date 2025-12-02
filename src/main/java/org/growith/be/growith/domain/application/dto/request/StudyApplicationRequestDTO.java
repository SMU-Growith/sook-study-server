package org.growith.be.growith.domain.application.dto.request;

import org.growith.be.growith.domain.application.entity.ApplicationStatus;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

public record StudyApplicationRequestDTO() {

    // 지원서 생성
    public record CreateStudyApplicationDTO(
            String nickName,
            StudentStatus studentStatus,
            String major,
            String phoneNumber,
            String motivation,
            ApplicationStatus applicationStatus
    ){}

    // 지원서 수정
    public record UpdateStudyApplicationDTO(
            ApplicationStatus status
    ){}
}
