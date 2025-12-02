package org.growith.be.growith.domain.study.controller;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.EmojiToggleRequest;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.study.dto.StudyDtlDto;
import org.growith.be.growith.domain.study.dto.StudyMemberDto;
import org.growith.be.growith.domain.study.dto.StudySessionCardDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.service.StudyService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final JournalEmojiService journalEmojiService; 

    @GetMapping
    public ResponseEntity<List<StudyCardDto>> getStudies(
            @RequestParam(required = false) List<String> fields,
            @RequestParam(required = false) List<String> formats,
            @RequestParam(required = false) List<String> styles,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ResponseEntity.ok(
            studyService.searchStudies(fields, formats, styles, status, keyword, sort, page, size)
        );
    }

    @GetMapping("/myStudies")
    public ApiResponse<List<StudyResponseDto.StudyCardDto>> getMyStudies(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "ACTIVE") String studyStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return ApiResponse.onSuccess(studyService.getMyStudies(String.valueOf(user.getUserId()), page, size, studyStatus));
    }

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

    @DeleteMapping("/{studyId}/withdraw")
    public ResponseEntity<Void> withdrawStudy(
            @PathVariable Long studyId,
            @AuthenticationPrincipal User user
    ) {
        studyService.withdrawStudy(studyId, String.valueOf(user.getUserId()));
        return ResponseEntity.ok().build();
    }
    //스터디 세션 리스트 조회
    @GetMapping("/{studyId}/sessions")
    public ResponseEntity<List<StudySessionCardDto>> getStudySessions(
            @PathVariable Long studyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return ResponseEntity.ok(studyService.getStudySessions(studyId, page, size));
    }

    // 스터디 세션 생성 (팀장권한)
    @PostMapping("/{studyId}/session")
    public ResponseEntity<StudySessionCardDto> createStudySession(
            @PathVariable Long studyId,
            @RequestBody StudySessionCardDto studySessionCardDto) {
        StudySessionCardDto createdSession = studyService.createStudySession(studyId, studySessionCardDto);
        return ResponseEntity.ok(createdSession);
    }

    // 스터디 세션 수정 (팀장권한)
    @PutMapping("/session/{sessionId}")
    public ResponseEntity<Void> updateStudySession(
            @PathVariable Long sessionId,
            @RequestBody StudySessionCardDto studySessionCardDto) {
        studyService.updateStudySession(sessionId, studySessionCardDto);
        return ResponseEntity.ok().build();
    }

    // 스터디 세션 조회 (팀장권한)
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<StudySessionCardDto> getStudySession(
            @PathVariable Long sessionId) {
        StudySessionCardDto session = studyService.getStudySession(sessionId);
        return ResponseEntity.ok(session);
    }

    // 스터디 규칙 조회
    @GetMapping("/{studyId}/rules")
    public ResponseEntity<Map<String, String>> getStudyRules(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyRules(studyId));
    }

    // 스터디 규칙 수정
    @PutMapping("/{studyId}/rules")
    public ResponseEntity<Void> updateStudyRules(
            @PathVariable Long studyId,
            @RequestBody Map<String, String> rules) {
        studyService.updateStudyRules(studyId, rules);
        return ResponseEntity.ok().build();
    }

    // 스터디 멤버 조회
    @GetMapping("/{studyId}/users")
    public ResponseEntity<List<StudyMemberDto>> getStudyMembers(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyMembers(studyId));
    }

    // 스터디 팀장 변경
    @PatchMapping("/{studyId}/changeLeader")
    public ResponseEntity<Void> changeStudyLeader(
            @PathVariable Long studyId,
            @AuthenticationPrincipal User user,
            @RequestParam Long newLeaderUserId) {
        studyService.changeStudyLeader(studyId, user.getUserId(), newLeaderUserId);
        return ResponseEntity.ok().build();
    }

    // 스터디 일지 제출
    @PostMapping("/session/{sessionId}/journal")
    public ResponseEntity<StudyJournalDto> createStudyJournal(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal User user,
            @RequestBody StudyJournalDto studyJournalDto) {
        StudyJournalDto createdJournal = studyService.createStudyJournal(sessionId, user.getUserId(), studyJournalDto);
        return ResponseEntity.ok(createdJournal);
    }

    // 스터디 일지 조회
    @GetMapping("/journal/{journalId}")
    public ResponseEntity<StudyJournalDto> getStudyJournal(@PathVariable Long journalId) {
        StudyJournalDto journal = studyService.getStudyJournal(journalId);
        return ResponseEntity.ok(journal);
    }

    // 스터디 일지 수정
    @PutMapping("/journal/{journalId}")
    public ResponseEntity<StudyJournalDto> updateStudyJournal(
            @PathVariable Long journalId,
            @RequestBody StudyJournalDto studyJournalDto) {
        StudyJournalDto updatedJournal = studyService.updateStudyJournal(journalId, studyJournalDto);
        return ResponseEntity.ok(updatedJournal);
    }

    // 스터디 일지 삭제
    @DeleteMapping("/journal/{journalId}")
    public ResponseEntity<Void> deleteStudyJournal(@PathVariable Long journalId) {
        studyService.deleteStudyJournal(journalId);
        return ResponseEntity.ok().build();
    }

    // 세션별 스터디 일지 목록 조회
    @GetMapping("/session/{sessionId}/journals")
    public ResponseEntity<List<StudyJournalListDto>> getStudyJournalsBySession(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        List<StudyJournalListDto> journals = studyService.getStudyJournalsBySession(sessionId, page, size);
        return ResponseEntity.ok(journals);
    }

    // 스터디일지 이모티콘 토글
    @PatchMapping("/journals/{studyJournalId}/emoji")
    public ResponseEntity<StudyJournalDto.EmojiCounts> toggleEmoji(
        @PathVariable Long studyJournalId,
        @AuthenticationPrincipal User user,
        @RequestBody EmojiToggleRequest request) {
    
        StudyJournalDto.EmojiCounts emojiCounts = journalEmojiService.toggleEmoji(
                studyJournalId, 
                user.getUserId(), 
                request.getEmojiType()
        );
    
        return ResponseEntity.ok(emojiCounts);
    }
   
}
