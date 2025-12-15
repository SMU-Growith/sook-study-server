package org.growith.be.growith.domain.personality.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

public record PersonalityRequestDto() {

    @Builder
    public record SubmitAnswersDto(
            @NotNull(message = "답변 목록은 필수입니다.")
            @Size(min = 6, max = 6, message = "6개의 답변이 필요합니다.")
            @Valid
            List<AnswerDto> answers
    ) {}

    @Builder
    public record AnswerDto(
            @NotNull(message = "질문 ID는 필수입니다.")
            Long questionId,
            
            @NotNull(message = "선택지 ID는 필수입니다.")
            Long optionId
    ) {}
}
