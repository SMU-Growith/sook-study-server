package org.growith.be.growith.domain.study.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.entity.enums.*;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

import java.time.LocalDate;
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
            List<StudyResponseDto.RuleDetailDTO> ruleDTO,
            Long userId,
            Boolean isScraped,
            LocalDate createdAt
    ){}

    @Builder
    public record RuleDetailDTO(
            RuleCategory ruleCategory,
            String description
    ){ }

    @Builder
    public record StudyPreviewDTOList(
            List<StudyPreviewDTO> studyPreviews,
            Integer listSize
    ){}


    // 내 스터디 조회에서 사용 
    @Builder
    public record StudyPreviewDTO(
            Long studyId,
            String title,
            String description,
            StudyStatus studyStatus,
            Long userId,
            Boolean isScraped,
            Long scrapCount,
            // 진행방식
            StudyFormat studyFormat,
            // 스터디 분야
            Long studyFieldId,
            String studyFieldName,
            StudyStyleCategory studyStyleCategory
    ){}

    public record StudyMemberDto(
            Long userId,
            String nickName,
            StudentStatus studentStatus,
            Major major,
            String majorName,
            String phoneNumber,
            String motivation
    ){}

    @Builder
    public record ChangedStudyStatus(
            StudyStatus studyStatus
    ){}

    @Builder
    public record UserStudyPreviewDto(
            Long studyId,
            StudyRole studyRole,
            String title,
            StudyStatus studyStatus,
            Long userId,
            String url,
            Long memberCount,
            Long studySessionCount,
            // 진행방식
            StudyFormat studyFormat,
            // 스터디 분야
            Long studyFieldId,
            String studyFieldName,
            StudyStyleCategory studyStyleCategory
    ){}

    @Builder
     public record StudyUsers(
             Long userId,
             StudyRole studyRole,
             String nickName,
             StudentStatus studentStatus,
             Major major,
             String phoneNumber,
             String motivation
     ){}

    @Builder
    public record StudyFieldDto(
            Long id,
            String name,
            List<StudyFieldDto> children
    ){}
    @Builder
    public record ToggleScrapResponseDto(
            Long studyId,
            Boolean isScraped,
            Long scrapCount
    ){}
}
