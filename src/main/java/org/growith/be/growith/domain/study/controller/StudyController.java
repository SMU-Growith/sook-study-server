package org.growith.be.growith.domain.study.controller;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.study.dto.StudyDtlDto;
import org.growith.be.growith.domain.study.service.StudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @GetMapping("/popular")
    public ResponseEntity<List<StudyCardDto>> getPopularStudies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(studyService.getPopularStudies(page, size));
    }

    @GetMapping("/new")
    public ResponseEntity<List<StudyCardDto>> getNewStudies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(studyService.getNewStudies(page, size));
    }

    @PostMapping
    public ResponseEntity<Void> createStudy(@RequestBody StudyDtlDto studyDtlDto) {
        studyService.createStudy(studyDtlDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDtlDto> getStudyDetail(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyDetail(studyId));
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(@PathVariable Long studyId, @RequestBody StudyDtlDto studyDtlDto) {
        studyService.updateStudy(studyId, studyDtlDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{studyId}/closed")
    public ResponseEntity<Void> closedStudy(@PathVariable Long studyId) {
        studyService.closedStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> deleteStudy(@PathVariable Long studyId) {
        studyService.deleteStudy(studyId);
        return ResponseEntity.ok().build();
    }
}