package org.growith.be.growith.domain.stamp.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.stamp.entity.enums.StampLevel;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;

import java.util.List;

public record StampResponseDto() {

    @Builder
    public record StampSummaryDto(
            int inProgressCount,    // 진행중인 스탬프 개수
            int completedCount,     // 완료한 스탬프 개수
            List<UserStampDto> stamps
    ) {}

    @Builder
    public record UserStampDto(
            StampType stampType,
            String stampName,
            String description,
            StampLevel achievedLevel,
            Boolean isAchieved,
            Boolean isCompleted,    // 해당 스탬프의 모든 레벨 완료 여부
            List<StampDetailDto> levels
    ) {}

    @Builder
    public record StampDetailDto(
            Long stampId,
            StampLevel level,
            String levelName,
            String levelDescription,
            Boolean isAchieved
    ) {}
}
