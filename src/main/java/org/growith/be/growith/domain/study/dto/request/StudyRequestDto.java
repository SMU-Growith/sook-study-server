package org.growith.be.growith.domain.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.entity.enums.*;

import java.util.List;

public record StudyRequestDto() {

    public record CreateStudyDTO(
            String title,
            String description,
            ContactType contactType,
            String url,
            String studyFieldName,
            StudyFormat studyFormat,
            StudyStyleCategory studyStyleCategory,
            List<RuleDTO> rules
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
            List<String> studyFieldNames,
            List<StudyFormat> studyFormats,
            List<StudyStyleCategory> studyStyleCategories,
            Boolean isRecruiting,
            String searchContent
    ){}

    public record MyStudiesRequest(
            StudyStatus studyStatus
    ){}

    public record UpdateRuleContentDTO(
            List<RuleDTO> rules
    ){}

}
