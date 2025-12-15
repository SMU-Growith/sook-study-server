package org.growith.be.growith.domain.personality.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.personality.entity.enums.PersonalityType;

import java.util.List;

public record PersonalityResponseDto() {

    @Builder
    public record ParticipantsDto(
            Integer totalParticipants
    ) {}

    @Builder
    public record QuestionListDto(
            List<QuestionDto> questions
    ) {}

    @Builder
    public record QuestionDto(
            Long questionId,
            Integer questionNumber,
            String questionText,
            List<OptionDto> options
    ) {}

    @Builder
    public record OptionDto(
            Long optionId,
            String optionText,
            PersonalityType personalityType
    ) {}

    @Builder
    public record TestResultDto(
            Long testId,
            ResultTypeDto resultType,
            ScoresDto scores
    ) {}

    @Builder
    public record ResultTypeDto(
            String typeCode,
            String typeName,
            String typeCategory,
            String tagline,
            String description,
            String caution
    ) {}

    @Builder
    public record ScoresDto(
            Integer plannedCount,
            Integer freeCount,
            Integer cooperativeCount,
            Integer achievementCount
    ) {}

    @Builder
    public record SaveResultDto(
            String message,
            String typeName
    ) {}

    @Builder
    public record UserPersonalityTypeDto(
            String typeName,
            String typeCategory
    ) {}
}
