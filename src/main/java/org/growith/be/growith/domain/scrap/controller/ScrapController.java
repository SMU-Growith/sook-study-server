package org.growith.be.growith.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.scrap.service.ScrapService;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ScrapController {
    private final ScrapService scrapService;

    // 스크랩 생성
    @PostMapping("/api/v1/studies/{studyId}/scrap")
    public ResponseEntity<Void> createScrap(@PathVariable Long studyId, @AuthenticationPrincipal User user) {
        scrapService.createScrap(studyId, user);
        return ResponseEntity.ok().build();
    }

    // 스크랩 삭제
    @DeleteMapping("/api/v1/users/scraps/{studyScrapId}")
    public ResponseEntity<Void> deleteScrap(@PathVariable Long studyScrapId, @AuthenticationPrincipal User user) {
        scrapService.deleteScrap(studyScrapId, user);
        return ResponseEntity.ok().build();
    }

    // 스크랩 목록 조회
    @GetMapping("/api/v1/users/scraps")
    public ResponseEntity<List<StudyCardDto>> getUserScraps(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size
    ) {
        Page<StudyScrap> scrapPage = scrapService.getUserScraps(user, page, size);
        List<StudyCardDto> dtos = scrapPage.getContent().stream()
                .map(scrap -> toStudyCardDto(scrap.getStudy()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Study -> StudyCardDto 변환 (필요시 StudyService에서 가져와도 됨)
    private StudyCardDto toStudyCardDto(Study study) {
        return StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .authorId(study.getUser().getUserId())
                .scrapCount(study.getScrapCount())
                .format(study.getFormat().name())
                .fieldName(study.getStudyField().getName())
                .styleNames(study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).collect(Collectors.toList()))
                .build();
    }
}

