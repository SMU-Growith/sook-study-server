package org.growith.be.growith.domain.scrap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.scrap.converter.ScrapConverter;
import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.scrap.service.ScrapService;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "스크랩 API")
public class ScrapController {
    private final ScrapService scrapService;

    // 스크랩 토글 (생성/삭제)
    @Operation(summary = "스터디 스크랩 생성/삭제 API by 윶", description = "스터디를 생성 혹은 삭제하는 API")
    @PostMapping("/studies/{studyId}/scrap/toggle")
    public ApiResponse<StudyResponseDto.ToggleScrapResponseDto> toggleScrap(
            @PathVariable Long studyId,
            @AuthenticatedUser User user
    ) {
        ScrapService.ToggleResult result = scrapService.toggleScrap(studyId, user.getId());
        return ApiResponse.onSuccess(ScrapConverter.toToggleScrapResponseDto(result.study(), result.isScraped()));
    }

    // // 스크랩 목록 조회
    // @Operation(summary = "스터디 스크랩 조회 API by 윶", description = "사용자가 스크랩한 스터디를 조회하는 API")
    // @GetMapping("/users/scraps")
    // public ApiResponse<StudyResponseDto.StudyPreviewDTOList> getUserScraps(
    //         @AuthenticatedUser User user,
    //         @PageableDefault(page = 0, size = 16,
    //                 sort = "createdAt", direction = Sort.Direction.DESC

    //         ) Pageable pageable
    // ) {
    //     Page<StudyScrap> scrapPage = scrapService.getUserScraps(user, pageable);
    //     return ApiResponse.onSuccess(ScrapConverter.toStudyPreviewList(scrapPage));
    // }

}
