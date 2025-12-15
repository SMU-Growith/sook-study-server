package org.growith.be.growith.domain.personality.service;

import org.growith.be.growith.domain.personality.dto.request.PersonalityRequestDto;
import org.growith.be.growith.domain.personality.dto.response.PersonalityResponseDto;

public interface PersonalityTestService {

    /**
     * 테스트 참여자 수 조회
     */
    PersonalityResponseDto.ParticipantsDto getParticipantsCount();

    /**
     * 테스트 질문 목록 조회
     */
    PersonalityResponseDto.QuestionListDto getQuestions();

    /**
     * 테스트 응답 제출 및 결과 계산
     */
    PersonalityResponseDto.TestResultDto submitAnswers(Long userId, PersonalityRequestDto.SubmitAnswersDto request);

    /**
     * 테스트 결과 저장 (내 유형으로 지정하기)
     */
    PersonalityResponseDto.SaveResultDto saveTestResult(Long userId, Long testId);
}
