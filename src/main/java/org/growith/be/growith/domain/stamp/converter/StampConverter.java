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
            StampType.CHEER, "응원고숙",
            StampType.SUPERSTAR, "슈퍼숙타"
    );

    public static StampResponseDto.UserStampDto toUserStampDto(
            StampType stampType, 
            List<Stamp> allStamps, 
            UserStamp userStamp
    ) {
        String stampName = STAMP_TYPE_NAMES.get(stampType);
        StampLevel achievedLevel = userStamp != null ? userStamp.getAchievedLevel() : StampLevel.NONE;
        Boolean isAchieved = userStamp != null && userStamp.getIsAchieved();

        // stamp 테이블에서 레벨 정보 가져오기
        List<StampResponseDto.StampDetailDto> levels = new ArrayList<>();
        StampLevel maxLevel = StampLevel.NONE;
        String description = null;
        
        for (Stamp stamp : allStamps) {
            if (stamp.getStampType() == stampType) {
                // description 가져오기 (stampLevel과 무관하게)
                if (description == null && stamp.getDescription() != null) {
                    description = stamp.getDescription();
                }
                
                // stampLevel이 null이 아니고 NONE이 아닌 경우에만 levels 배열에 추가
                if (stamp.getStampLevel() != null && stamp.getStampLevel() != StampLevel.NONE) {
                    boolean levelAchieved = achievedLevel.ordinal() >= stamp.getStampLevel().ordinal();
                    
                    // 최대 레벨 찾기
                    if (stamp.getStampLevel().ordinal() > maxLevel.ordinal()) {
                        maxLevel = stamp.getStampLevel();
                    }
                    
                    levels.add(StampResponseDto.StampDetailDto.builder()
                            .stampId(stamp.getId())
                            .level(stamp.getStampLevel())
                            .levelName(stamp.getName())
                            .levelDescription(stamp.getLevelDescription())
                            .isAchieved(levelAchieved)
                            .build());
                }
            }
        }

        // 완료 여부 계산
        // 웰컴숙: 레벨이 없으므로 isAchieved == true이면 isCompleted == true
        // 다른 스탬프: 최대 레벨까지 모두 달성했는지 확인
        Boolean isCompleted;
        if (stampType == StampType.WELCOME) {
            isCompleted = isAchieved;  // 웰컴숙은 달성 즉시 완료
        } else {
            isCompleted = achievedLevel.ordinal() >= maxLevel.ordinal() && maxLevel != StampLevel.NONE;
        }

        return StampResponseDto.UserStampDto.builder()
                .stampType(stampType)
                .stampName(stampName)
                .description(description)
                .achievedLevel(achievedLevel)
                .isAchieved(isAchieved)
                .isCompleted(isCompleted)
                .levels(levels)
                .build();
    }

}
