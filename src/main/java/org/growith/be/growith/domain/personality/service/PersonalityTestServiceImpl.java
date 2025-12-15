package org.growith.be.growith.domain.personality.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.personality.converter.PersonalityConverter;
import org.growith.be.growith.domain.personality.dto.request.PersonalityRequestDto;
import org.growith.be.growith.domain.personality.dto.response.PersonalityResponseDto;
import org.growith.be.growith.domain.personality.entity.*;
import org.growith.be.growith.domain.personality.entity.enums.PersonalityType;
import org.growith.be.growith.domain.personality.entity.enums.ResultTypeCode;
import org.growith.be.growith.domain.personality.repository.*;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonalityTestServiceImpl implements PersonalityTestService {

    private final PersonalityQuestionRepository questionRepository;
    private final PersonalityOptionRepository optionRepository;
    private final PersonalityResultTypeRepository resultTypeRepository;
    private final UserPersonalityTestRepository testRepository;
    private final PersonalityTestStatsRepository statsRepository;
    private final UserRepository userRepository;

    @Override
    public PersonalityResponseDto.ParticipantsDto getParticipantsCount() {
        PersonalityTestStats stats = statsRepository.findById(1L)
                .orElseGet(() -> PersonalityTestStats.builder().id(1L).totalParticipants(0).build());
        
        return PersonalityConverter.toParticipantsDto(stats.getTotalParticipants());
    }

    @Override
    public PersonalityResponseDto.QuestionListDto getQuestions() {
        List<PersonalityQuestion> questions = questionRepository.findAllWithOptions();
        return PersonalityConverter.toQuestionListDto(questions);
    }

    @Override
    @Transactional
    public PersonalityResponseDto.TestResultDto submitAnswers(
            Long userId,
            PersonalityRequestDto.SubmitAnswersDto request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 1. 각 성향별 카운트 계산
        Map<PersonalityType, Integer> typeCounts = new HashMap<>();
        typeCounts.put(PersonalityType.PLANNED, 0);
        typeCounts.put(PersonalityType.FREE, 0);
        typeCounts.put(PersonalityType.COOPERATIVE, 0);
        typeCounts.put(PersonalityType.ACHIEVEMENT, 0);

        List<UserPersonalityAnswer> answers = new ArrayList<>();
        
        for (PersonalityRequestDto.AnswerDto answerDto : request.answers()) {
            PersonalityQuestion question = questionRepository.findById(answerDto.questionId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
            
            PersonalityOption option = optionRepository.findById(answerDto.optionId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

            // 성향 타입 카운트 증가
            PersonalityType type = option.getPersonalityType();
            typeCounts.put(type, typeCounts.get(type) + 1);
        }

        // 2. UserPersonalityTest 생성
        UserPersonalityTest test = UserPersonalityTest.builder()
                .user(user)
                .plannedCount(typeCounts.get(PersonalityType.PLANNED))
                .freeCount(typeCounts.get(PersonalityType.FREE))
                .cooperativeCount(typeCounts.get(PersonalityType.COOPERATIVE))
                .achievementCount(typeCounts.get(PersonalityType.ACHIEVEMENT))
                .isSaved(false)
                .build();

        // 3. 결과 유형 결정
        ResultTypeCode resultTypeCode = determineResultType(typeCounts);
        PersonalityResultType resultType = resultTypeRepository.findByTypeCode(resultTypeCode)
                .orElseThrow(() -> new IllegalStateException("Result type not found: " + resultTypeCode));

        test.setResultType(resultType);
        testRepository.save(test);

        // 4. 답변 저장
        for (PersonalityRequestDto.AnswerDto answerDto : request.answers()) {
            PersonalityQuestion question = questionRepository.findById(answerDto.questionId()).get();
            PersonalityOption option = optionRepository.findById(answerDto.optionId()).get();

            UserPersonalityAnswer answer = UserPersonalityAnswer.builder()
                    .test(test)
                    .question(question)
                    .option(option)
                    .build();
            
            answers.add(answer);
        }
        test.getAnswers().addAll(answers);

        // 5. 참여자 수 증가
        PersonalityTestStats stats = statsRepository.findById(1L)
                .orElseGet(() -> {
                    PersonalityTestStats newStats = PersonalityTestStats.builder()
                            .id(1L)
                            .totalParticipants(0)
                            .build();
                    return statsRepository.save(newStats);
                });
        stats.incrementParticipants();

        return PersonalityConverter.toTestResultDto(test, resultType);
    }

    @Override
    @Transactional
    public PersonalityResponseDto.SaveResultDto saveTestResult(Long userId, Long testId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        UserPersonalityTest test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found"));

        if (!test.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Test does not belong to user");
        }

        // User 테이블에 성향 유형 저장
        user.setPersonalityResultType(test.getResultType());
        
        // 테스트 결과 저장 상태 업데이트
        test.markAsSaved();

        return PersonalityConverter.toSaveResultDto(test.getResultType().getTypeName());
    }

    /**
     * 성향 타입 카운트를 기반으로 결과 유형 결정
     */
    private ResultTypeCode determineResultType(Map<PersonalityType, Integer> typeCounts) {
        // 카운트를 내림차순으로 정렬
        List<Map.Entry<PersonalityType, Integer>> sortedEntries = typeCounts.entrySet().stream()
                .sorted(Map.Entry.<PersonalityType, Integer>comparingByValue().reversed())
                .toList();

        int firstCount = sortedEntries.get(0).getValue();
        int secondCount = sortedEntries.get(1).getValue();
        
        PersonalityType firstType = sortedEntries.get(0).getKey();
        PersonalityType secondType = sortedEntries.get(1).getKey();

        // 균형형: 모든 성향이 고르게 분포 (각각 1개 이상)
        boolean isBalanced = typeCounts.values().stream().allMatch(count -> count >= 1 && count <= 2);
        if (isBalanced && firstCount == 2 && secondCount == 1) {
            // 2-1-1-2 또는 2-2-1-1 패턴이 아닌 경우
            long countOfTwo = typeCounts.values().stream().filter(c -> c == 2).count();
            if (countOfTwo != 2) {
                return ResultTypeCode.ALLROUNDER;
            }
        }

        // 단일 유형: 한 성향이 3개 이상
        if (firstCount >= 3) {
            return switch (firstType) {
                case PLANNED -> ResultTypeCode.METICULOUS;
                case FREE -> ResultTypeCode.FLUTTERING;
                case COOPERATIVE -> ResultTypeCode.WARM;
                case ACHIEVEMENT -> ResultTypeCode.BRILLIANT;
            };
        }

        // 혼합 유형: 두 성향이 각각 2개씩
        if (firstCount == 2 && secondCount == 2) {
            return getMixedType(firstType, secondType);
        }

        // 기본값 (예외 처리)
        return ResultTypeCode.ALLROUNDER;
    }

    /**
     * 두 성향의 조합으로 혼합 유형 결정
     */
    private ResultTypeCode getMixedType(PersonalityType type1, PersonalityType type2) {
        Set<PersonalityType> types = Set.of(type1, type2);

        if (types.equals(Set.of(PersonalityType.PLANNED, PersonalityType.FREE))) {
            return ResultTypeCode.MYSTERIOUS;
        }
        if (types.equals(Set.of(PersonalityType.PLANNED, PersonalityType.COOPERATIVE))) {
            return ResultTypeCode.RELIABLE;
        }
        if (types.equals(Set.of(PersonalityType.PLANNED, PersonalityType.ACHIEVEMENT))) {
            return ResultTypeCode.METICULOUS_STRICT;
        }
        if (types.equals(Set.of(PersonalityType.FREE, PersonalityType.COOPERATIVE))) {
            return ResultTypeCode.CHEERFUL;
        }
        if (types.equals(Set.of(PersonalityType.FREE, PersonalityType.ACHIEVEMENT))) {
            return ResultTypeCode.SPARKLING;
        }
        if (types.equals(Set.of(PersonalityType.COOPERATIVE, PersonalityType.ACHIEVEMENT))) {
            return ResultTypeCode.PROUD;
        }

        return ResultTypeCode.ALLROUNDER;
    }
}
