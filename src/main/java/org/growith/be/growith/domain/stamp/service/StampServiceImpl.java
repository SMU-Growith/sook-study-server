package org.growith.be.growith.domain.stamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.stamp.converter.StampConverter;
import org.growith.be.growith.domain.stamp.dto.response.StampResponseDto;
import org.growith.be.growith.domain.stamp.entity.Stamp;
import org.growith.be.growith.domain.stamp.entity.UserStamp;
import org.growith.be.growith.domain.stamp.entity.enums.StampLevel;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;
import org.growith.be.growith.domain.stamp.repository.StampRepository;
import org.growith.be.growith.domain.stamp.repository.UserStampRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {

    private final UserStampRepository userStampRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;

    @Override
    public StampResponseDto.StampSummaryDto getUserStamps(Long userId) {
        // 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 모든 스탬프 조회
        List<Stamp> allStamps = stampRepository.findAll();

        // 사용자의 스탬프 진행 상황 조회
        List<UserStamp> userStamps = userStampRepository.findAllByUserId(userId);
        
        // StampType별로 UserStamp 매핑
        Map<StampType, UserStamp> userStampMap = userStamps.stream()
                .collect(Collectors.toMap(
                        UserStamp::getStampType,
                        us -> us,
                        (existing, replacement) -> existing
                ));

        // 모든 StampType에 대해 DTO 생성
        List<StampResponseDto.UserStampDto> stampDtos = new ArrayList<>();
        int inProgressCount = 0;
        int completedCount = 0;
        
        for (StampType stampType : StampType.values()) {
            UserStamp userStamp = userStampMap.get(stampType);
            StampResponseDto.UserStampDto stampDto = StampConverter.toUserStampDto(stampType, allStamps, userStamp);
            stampDtos.add(stampDto);
            
            // 통계 계산
            if (stampDto.isCompleted()) {
                completedCount++;  // 모든 레벨 완료
            } else if (stampDto.isAchieved()) {
                inProgressCount++;  // 일부 레벨만 완료 (진행중)
            }
        }

        return StampResponseDto.StampSummaryDto.builder()
                .inProgressCount(inProgressCount)
                .completedCount(completedCount)
                .stamps(stampDtos)
                .build();
    }

    @Override
    @Transactional
    public void updateStampProgress(Long userId, StampType stampType, int count) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 달성해야 할 레벨 결정
        StampLevel targetLevel = determineStampLevel(stampType, count);
        
        // 웰컴숙이 아닌 경우, NONE 레벨이면 조건 미달성으로 처리
        if (stampType != StampType.WELCOME && targetLevel == StampLevel.NONE) {
            return; // 조건 미달성
        }

        // 사용자의 해당 타입 스탬프 조회 또는 생성
        Optional<UserStamp> existingUserStamp = userStampRepository.findByUserIdAndStampType(userId, stampType);

        if (existingUserStamp.isPresent()) {
            UserStamp userStamp = existingUserStamp.get();
            // 웰컴숙이 아닌 경우에만 레벨 비교 후 업데이트
            if (stampType != StampType.WELCOME && targetLevel.ordinal() > userStamp.getAchievedLevel().ordinal()) {
                userStamp.updateLevel(targetLevel);
                userStampRepository.save(userStamp);
                log.info("Updated stamp for user {}: {} to level {}", userId, stampType, targetLevel);
            }
        } else {
            // 새로운 UserStamp 생성
            UserStamp newUserStamp = UserStamp.builder()
                    .user(user)
                    .stampType(stampType)
                    .achievedLevel(targetLevel)
                    .isAchieved(true)
                    .build();

            userStampRepository.save(newUserStamp);
            log.info("Created new stamp for user {}: {} at level {}", userId, stampType, targetLevel);
        }
    }

    // 카운트에 따라 달성 레벨 결정
    private StampLevel determineStampLevel(StampType stampType, int count) {
        return switch (stampType) {
            case WELCOME -> StampLevel.NONE;  // 웰컴숙은 레벨이 없음 (회원가입 시 자동 달성)
            case LEADER -> {
                if (count >= 3) yield StampLevel.LEVEL_2;
                else if (count >= 1) yield StampLevel.LEVEL_1;
                else yield StampLevel.NONE;
            }
            case RECORD -> {
                if (count >= 10) yield StampLevel.LEVEL_2;
                else if (count >= 3) yield StampLevel.LEVEL_1;
                else yield StampLevel.NONE;
            }
            case CHEER -> {
                if (count >= 10) yield StampLevel.LEVEL_2;
                else if (count >= 3) yield StampLevel.LEVEL_1;
                else yield StampLevel.NONE;
            }
            case SUPERSTAR -> {
                if (count >= 20) yield StampLevel.LEVEL_2;
                else if (count >= 10) yield StampLevel.LEVEL_1;
                else yield StampLevel.NONE;
            }
        };
    }
}
