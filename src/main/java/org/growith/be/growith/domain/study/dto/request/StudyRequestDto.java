package org.growith.be.growith.domain.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.entity.enums.*;

import java.util.List;

public record StudyRequestDto() {

    public record CreateStudyDTO(
            @NotBlank
            String title,
            String description,
            @NotBlank
            ContactType contactType,
            @NotBlank
            String url,
            @NotBlank
            Long studyFieldId,
            @NotBlank
            StudyFormat studyFormat,
            @NotBlank
            StudyStyleCategory studyStyleCategory,
            List<RuleDTO> ruleDTO
    ){}

    public record RuleDTO(
            RuleCategory ruleCategory,
            String description
    ){ }

    public record UpdateStudyDTO(
            String title,
            String description,
            ContactType contactType,
            String url,
            String studyFieldName,
            StudyFormat studyFormat,
            StudyStyleCategory studyStyleCategory,
            StudyStatus studyStatus,
            List<RuleDTO> ruleDTO
    ){}

    public record ChangeStudyStatusDTO(
            StudyStatus studyStatus
    ){}

    public record SearchStudyCondition(
            List<Long> studyFieldIds,
            List<StudyFormat> studyFormats,
            List<StudyStyleCategory> studyStyleCategories,
            StudyStatus studyStatus,
            String searchContent
    ){}


    public record UpdateRuleContentDTO(
            String description
    ){}

}
