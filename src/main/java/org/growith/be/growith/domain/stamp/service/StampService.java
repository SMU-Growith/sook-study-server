package org.growith.be.growith.domain.stamp.service;

import org.growith.be.growith.domain.stamp.dto.response.StampResponseDto;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;

import java.util.List;

public interface StampService {
    // 사용자의 모든 스탬프 조회 (통계 포함)
    StampResponseDto.StampSummaryDto getUserStamps(Long userId);
    
    // 스탬프 업데이트 (조건 달성 시 호출)
    void updateStampProgress(Long userId, StampType stampType, int count);
}
