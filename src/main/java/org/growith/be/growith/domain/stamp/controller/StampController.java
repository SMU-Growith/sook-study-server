package org.growith.be.growith.domain.stamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.stamp.dto.response.StampResponseDto;
import org.growith.be.growith.domain.stamp.service.StampService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/stamps")
@RequiredArgsConstructor
@Tag(name = "스탬프 API")
public class StampController {

    private final StampService stampService;

    @Operation(
            summary = "내 스탬프 조회 API",
            description = "로그인한 사용자의 모든 스탬프 획득 현황을 조회합니다. 진행중/완료 스탬프 개수와 각 스탬프 타입별 달성 레벨 및 상세 정보를 반환합니다."
    )
    @GetMapping
    public ApiResponse<StampResponseDto.StampSummaryDto> getMyStamps(
            @AuthenticatedUser User user,
            @RequestParam(required = false) Long userId
    ) {
        // userId가 제공되면 해당 사용자의 스탬프 조회, 없으면 본인 스탬프 조회
        Long targetUserId = (userId != null) ? userId : user.getId();
        StampResponseDto.StampSummaryDto summary = stampService.getUserStamps(targetUserId);
        return ApiResponse.onSuccess(summary);
    }
}
