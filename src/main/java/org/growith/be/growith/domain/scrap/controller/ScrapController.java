package org.growith.be.growith.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.scrap.dto.ScrapListResponseDto;
import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.scrap.service.ScrapService;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ScrapController {
    private final ScrapService scrapService;

    // 스크랩 토글 (생성/삭제)
    @PostMapping("studies/{studyId}/scrap/toggle")
    public ApiResponse<Void> toggleScrap(
            @PathVariable Long studyId,
            @AuthenticationPrincipal User user
    ) {
        scrapService.toggleScrap(studyId, user.getId());
        return ApiResponse.onSuccess(null);
    }

    // 스크랩 목록 조회
    @GetMapping("users/scraps")
    public ApiResponse<ScrapListResponseDto> getUserScraps(
            @AuthenticationPrincipal User user,
            @PageableDefault(page = 0, size = 16) Pageable pageable
    ) {
        Page<StudyScrap> scrapPage = scrapService.getUserScraps(user, pageable);
        List<StudyCardDto> dtos = scrapPage.getContent().stream()
                .map(scrap -> toStudyCardDto(scrap.getStudy()))
                .collect(Collectors.toList());
        
        ScrapListResponseDto response = ScrapListResponseDto.builder()
                .scraps(dtos)
                .totalCount(scrapPage.getTotalElements())
                .build();
        
        return ApiResponse.onSuccess(response);
    }

    // Study -> StudyCardDto 변환 (필요시 StudyService에서 가져와도 됨)
    private StudyCardDto toStudyCardDto(Study study) {
        return StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .authorId(String.valueOf(study.getUser().getId()))
                .scrapCount(study.getScrapCount())
                .format(study.getStudyFormat().name())
                .fieldName(study.getStudyField().getName())
                .styleNames(study.getStudyStyleCategory())
                .build();
    }
}
