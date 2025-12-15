package org.growith.be.growith.domain.personality.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.personality.dto.request.PersonalityRequestDto;
import org.growith.be.growith.domain.personality.dto.response.PersonalityResponseDto;
import org.growith.be.growith.domain.personality.service.PersonalityTestService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personality-test")
@RequiredArgsConstructor
@Tag(name = "성향 테스트 API")
public class PersonalityTestController {

    private final PersonalityTestService personalityTestService;

    @Operation(summary = "테스트 참여자 수 조회", description = "현재까지 테스트에 참여한 누적 인원 수를 조회합니다.")
    @GetMapping("/participants")
    public ApiResponse<PersonalityResponseDto.ParticipantsDto> getParticipants() {
        return ApiResponse.onSuccess(personalityTestService.getParticipantsCount());
    }

    @Operation(summary = "테스트 질문 목록 조회", description = "성향 테스트 질문 6개와 각 질문의 선택지 4개를 조회합니다.")
    @GetMapping("/questions")
    public ApiResponse<PersonalityResponseDto.QuestionListDto> getQuestions() {
        return ApiResponse.onSuccess(personalityTestService.getQuestions());
    }

    @Operation(summary = "테스트 응답 제출 및 결과 계산", description = "사용자의 6개 응답을 제출하고 성향 유형 결과를 계산합니다.")
    @PostMapping("/submit")
    public ApiResponse<PersonalityResponseDto.TestResultDto> submitAnswers(
            @AuthenticatedUser User user,
            @RequestBody @Valid PersonalityRequestDto.SubmitAnswersDto request
    ) {
        return ApiResponse.onSuccess(personalityTestService.submitAnswers(user.getId(), request));
    }

    @Operation(summary = "테스트 결과 저장", description = "'내 유형으로 지정하기' 버튼 클릭 시 결과를 사용자 프로필에 저장합니다.")
    @PostMapping("/{testId}/save")
    public ApiResponse<PersonalityResponseDto.SaveResultDto> saveTestResult(
            @AuthenticatedUser User user,
            @PathVariable Long testId
    ) {
        return ApiResponse.onSuccess(personalityTestService.saveTestResult(user.getId(), testId));
    }
}
