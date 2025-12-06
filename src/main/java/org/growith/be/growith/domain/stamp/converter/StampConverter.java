package org.growith.be.growith.domain.stamp.converter;

import org.growith.be.growith.domain.stamp.dto.response.StampResponseDto;
import org.growith.be.growith.domain.stamp.entity.Stamp;
import org.growith.be.growith.domain.stamp.entity.UserStamp;
import org.growith.be.growith.domain.stamp.entity.enums.StampLevel;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StampConverter {

    // 스탬프 타입별 이름 매핑
    private static final Map<StampType, String> STAMP_TYPE_NAMES = Map.of(
            StampType.WELCOME, "웰컴숙",
            StampType.LEADER, "리더숙",
            StampType.RECORD, "기록숙",
            StampType.CHEER, "응원 고숙",
            StampType.SUPERSTAR, "슈퍼숙타"
    );

    private record StampInfo(String name, String description) {}

    // 스탬프 레벨별 정보(이름, 설명) 매핑
    private static final Map<StampType, Map<StampLevel, StampInfo>> STAMP_LEVEL_INFO = Map.of(
            StampType.WELCOME, Map.of(
                    StampLevel.LEVEL_1, new StampInfo("회원가입 완료", "회원가입을 축하합니다!")
            ),
            StampType.LEADER, Map.of(
                    StampLevel.LEVEL_1, new StampInfo("과대송", "스터디 1회 개설"),
                    StampLevel.LEVEL_2, new StampInfo("교수송", "스터디 3회 개설")
            ),
            StampType.RECORD, Map.of(
                    StampLevel.LEVEL_1, new StampInfo("장학생", "학습일지 3회 작성"),
                    StampLevel.LEVEL_2, new StampInfo("수석송", "학습일지 10회 작성")
            ),
            StampType.CHEER, Map.of(
                    StampLevel.LEVEL_1, new StampInfo("응원단송", "응원하기 3명에게 진행"),
                    StampLevel.LEVEL_2, new StampInfo("치어리더송", "응원하기 10명에게 진행")
            ),
            StampType.SUPERSTAR, Map.of(
                    StampLevel.LEVEL_1, new StampInfo("라이징송", "내 스터디 일지에 10개 응원 누적"),
                    StampLevel.LEVEL_2, new StampInfo("인싸송", "내 스터디 일지에 20개 응원 누적")
            )
    );

    public static StampResponseDto.UserStampDto toUserStampDto(
            StampType stampType, 
            List<Stamp> allStamps, 
            UserStamp userStamp
    ) {
        String stampName = STAMP_TYPE_NAMES.get(stampType);
        StampLevel achievedLevel = userStamp != null ? userStamp.getAchievedLevel() : StampLevel.NONE;
        Boolean isAchieved = userStamp != null && userStamp.getIsAchieved();

        // 해당 타입의 모든 레벨 정보 생성
        List<StampResponseDto.StampDetailDto> levels = new ArrayList<>();
        StampLevel maxLevel = StampLevel.NONE;
        
        for (Stamp stamp : allStamps) {
            if (stamp.getStampType() == stampType && stamp.getStampLevel() != StampLevel.NONE) {
                boolean levelAchieved = achievedLevel.ordinal() >= stamp.getStampLevel().ordinal();
                
                // 스탬프 정보 가져오기
                Map<StampLevel, StampInfo> levelInfoMap = STAMP_LEVEL_INFO.get(stampType);
                StampInfo stampInfo = levelInfoMap != null ? levelInfoMap.get(stamp.getStampLevel()) : new StampInfo("", "");

                // 최대 레벨 찾기 (기존 로직 유지)
                if (stamp.getStampLevel().ordinal() > maxLevel.ordinal()) {
                    maxLevel = stamp.getStampLevel();
                }
                
                levels.add(StampResponseDto.StampDetailDto.builder()
                        .stampId(stamp.getId())
                        .level(stamp.getStampLevel())
                        .levelName(stampInfo.name())
                        .levelDescription(stampInfo.description())
                        .isAchieved(levelAchieved)
                        .build());
            }
        }

        // 완료 여부: 최대 레벨까지 모두 달성했는지 확인
        Boolean isCompleted = achievedLevel.ordinal() >= maxLevel.ordinal() && maxLevel != StampLevel.NONE;

        return StampResponseDto.UserStampDto.builder()
                .stampType(stampType)
                .stampName(stampName)
                .description(getStampTypeDescription(stampType))
                .achievedLevel(achievedLevel)
                .isAchieved(isAchieved)
                .isCompleted(isCompleted)
                .levels(levels)
                .build();
    }

    private static String getStampTypeDescription(StampType stampType) {
        return switch (stampType) {
            case WELCOME -> "회원가입을 축하합니다!";
            case LEADER -> "스터디를 개설하여 리더십을 발휘하세요";
            case RECORD -> "학습일지를 작성하여 성장을 기록하세요";
            case CHEER -> "다른 사람을 응원하여 함께 성장하세요";
            case SUPERSTAR -> "많은 응원을 받아 인기 스타가 되세요";
        };
    }


}
