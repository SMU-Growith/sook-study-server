package org.growith.be.growith.domain.study.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.study.entity.enums.*;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

import java.util.List;

public record StudyResponseDto() {

    @Builder
    public record StudyDetail(
            String title,
            String description,
            StudyStatus studyStatus,
            ContactType contactType,
            String url,
            Boolean isRecruiting,
            Long studyFieldId,
            String studyFieldName,
            StudyFormat studyFormat,
            StudyStyleCategory studyStyleCategory,
            List<StudyResponseDto.RuleDetailDTO> ruleDTO
    ){}

    @Builder
    public record RuleDetailDTO(
            RuleCategory ruleCategory,
            String description
    ){ }


    // 내 스터디 조회에서 사용
    @Builder
    public record StudyCardDto ( // record -> StudyCardDto.getStudyId get
        Long studyId,
        StudyStatus studyStatus,
        String title,
        String authorId,
        Long scrapCount,
        String format, // 진행방식
        String fieldName, // 분야
        List<String> styleNames,

        Integer memberCount,    // 스터디원 수
        Integer studyDays,  // 스터디 진행 일수
        String userRole // 사용자의 스터디 내 역할
    ){}

    public record StudyMemberDto(
        Long userId,
        String nickName,
        StudentStatus studentStatus,
        Major major,
        String phoneNumber,
        String motivation
    ){}

    @Builder
    public record ChangedStudyStatus(
            StudyStatus studyStatus
    ){}
}
