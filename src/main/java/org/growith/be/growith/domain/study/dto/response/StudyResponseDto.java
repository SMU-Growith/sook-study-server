package org.growith.be.growith.domain.study.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.study.entity.StudyField;
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

    @Builder
    public record StudyPreviewDTOList(
            List<StudyPreviewDTO> studyPreviews,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ){}


    // 내 스터디 조회에서 사용 
    @Builder
    public record StudyPreviewDTO(
            Long studyId,
            String title,
            StudyStatus studyStatus,
            Long userId,
            String url,
            Boolean isScraped,
            Long scrapCount,
            // 진행방식
            StudyFormat studyFormat,
            // 스터디 분야
            StudyField studyField,
            StudyStyleCategory studyStyleCategory
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
