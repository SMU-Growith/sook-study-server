package org.growith.be.growith.domain.personality.converter;

import org.growith.be.growith.domain.personality.dto.response.PersonalityResponseDto;
import org.growith.be.growith.domain.personality.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class PersonalityConverter {

    public static PersonalityResponseDto.QuestionListDto toQuestionListDto(List<PersonalityQuestion> questions) {
        List<PersonalityResponseDto.QuestionDto> questionDtos = questions.stream()
                .map(PersonalityConverter::toQuestionDto)
                .collect(Collectors.toList());

        return PersonalityResponseDto.QuestionListDto.builder()
                .questions(questionDtos)
                .build();
    }

    public static PersonalityResponseDto.QuestionDto toQuestionDto(PersonalityQuestion question) {
        List<PersonalityResponseDto.OptionDto> optionDtos = question.getOptions().stream()
                .map(PersonalityConverter::toOptionDto)
                .collect(Collectors.toList());

        return PersonalityResponseDto.QuestionDto.builder()
                .questionId(question.getId())
                .questionNumber(question.getQuestionNumber())
                .questionText(question.getQuestionText())
                .options(optionDtos)
                .build();
    }

    public static PersonalityResponseDto.OptionDto toOptionDto(PersonalityOption option) {
        return PersonalityResponseDto.OptionDto.builder()
                .optionId(option.getId())
                .optionText(option.getOptionText())
                .personalityType(option.getPersonalityType())
                .build();
    }

    public static PersonalityResponseDto.TestResultDto toTestResultDto(
            UserPersonalityTest test,
            PersonalityResultType resultType
    ) {
        return PersonalityResponseDto.TestResultDto.builder()
                .testId(test.getId())
                .resultType(toResultTypeDto(resultType))
                .scores(toScoresDto(test))
                .build();
    }

    public static PersonalityResponseDto.ResultTypeDto toResultTypeDto(PersonalityResultType resultType) {
        return PersonalityResponseDto.ResultTypeDto.builder()
                .typeCode(resultType.getTypeCode().name())
                .typeName(resultType.getTypeName())
                .typeCategory(resultType.getTypeCategory())
                .tagline(resultType.getTagline())
                .description(resultType.getDescription())
                .caution(resultType.getCaution())
                .build();
    }

    public static PersonalityResponseDto.ScoresDto toScoresDto(UserPersonalityTest test) {
        return PersonalityResponseDto.ScoresDto.builder()
                .plannedCount(test.getPlannedCount())
                .freeCount(test.getFreeCount())
                .cooperativeCount(test.getCooperativeCount())
                .achievementCount(test.getAchievementCount())
                .build();
    }

    public static PersonalityResponseDto.ParticipantsDto toParticipantsDto(Integer totalParticipants) {
        return PersonalityResponseDto.ParticipantsDto.builder()
                .totalParticipants(totalParticipants)
                .build();
    }

    public static PersonalityResponseDto.SaveResultDto toSaveResultDto(String typeName) {
        return PersonalityResponseDto.SaveResultDto.builder()
                .message("성향 유형이 저장되었습니다.")
                .typeName(typeName)
                .build();
    }

    public static PersonalityResponseDto.UserPersonalityTypeDto toUserPersonalityTypeDto(PersonalityResultType resultType) {
        if (resultType == null) {
            return null;
        }
        
        return PersonalityResponseDto.UserPersonalityTypeDto.builder()
                .typeName(resultType.getTypeName())
                .typeCategory(resultType.getTypeCategory())
                .build();
    }
}
