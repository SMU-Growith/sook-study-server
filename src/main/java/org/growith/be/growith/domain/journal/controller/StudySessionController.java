package org.growith.be.growith.domain.journal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.EmojiToggleRequest;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.journal.service.command.StudyJournalCommandService;
import org.growith.be.growith.domain.journal.service.query.StudyJournalQueryService;
import org.growith.be.growith.domain.study.service.command.StudyCommandService;
import org.growith.be.growith.domain.study.service.query.StudyQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
@Tag(name = "스터디 세션 일지 API")
public class StudySessionController {

    private final StudyJournalCommandService studyJournalCommandService;
    private final StudyJournalQueryService studyJournalQueryService;
    private final JournalEmojiService journalEmojiService;


    //스터디 세션 리스트 조회
    @GetMapping("/{studyId}/sessions")
    public ApiResponse<List<StudySessionCardDto>> getStudySessions(
            @PathVariable Long studyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        studyJournalQueryService.getStudySessions(studyId, page, size);
        return  ApiResponse.onSuccess(null);
    }

    // 스터디 세션 생성 (팀장권한)
    @PostMapping("/{studyId}/session")
    public ApiResponse<StudySessionCardDto> createStudySession(
            @PathVariable Long studyId,
            @RequestBody StudySessionCardDto studySessionCardDto) {
        StudySessionCardDto createdSession = studyJournalCommandService.createStudySession(studyId, studySessionCardDto);
        return  ApiResponse.onSuccess(createdSession);
    }

    // 스터디 세션 수정 (팀장권한)
    @PutMapping("/session/{sessionId}")
    public ApiResponse<Void> updateStudySession(
            @PathVariable Long sessionId,
            @RequestBody StudySessionCardDto studySessionCardDto) {
        studyJournalCommandService.updateStudySession(sessionId, studySessionCardDto);
        return  ApiResponse.onSuccess(null);
    }

    // 스터디 세션 조회 (팀장권한)
    @GetMapping("/session/{sessionId}")
    public ApiResponse<List<StudySessionCardDto>> getStudySession(
            @PathVariable Long sessionId,
            @PageableDefault(page = 0, size = 6) Pageable pageable
    ) {
        List<StudySessionCardDto> studySessions = studyJournalQueryService.getStudySessions(sessionId, pageable.getPageNumber(), pageable.getPageSize());
        return  ApiResponse.onSuccess(studySessions);
    }

    // 스터디 세션 삭제 (팀장권한)
    @Operation(
            summary = "스터디 세션 삭제 API",
            description = "팀장 권한을 가진 사용자만 해당 스터디의 세션을 삭제할 수 있습니다."
    )
    @DeleteMapping("/session/{sessionId}")
    public ApiResponse<Void> deleteStudySession(
            @PathVariable Long sessionId,
            @AuthenticatedUser User user
    ) {
        studyJournalCommandService.deleteStudySession(sessionId, user.getId());
        return ApiResponse.onSuccess(null);
    }

    // 스터디 일지 제출
    @PostMapping("/session/{sessionId}/journal")
    public ApiResponse<StudyJournalDto> createStudyJournal(
            @PathVariable Long sessionId,
            @AuthenticatedUser User user,
            @RequestBody StudyJournalDto studyJournalDto) {
        StudyJournalDto createdJournal = studyJournalCommandService.createStudyJournal(sessionId, user.getId(), studyJournalDto);
        return  ApiResponse.onSuccess(createdJournal);
    }

    // 스터디 일지 조회
    @GetMapping("/journal/{journalId}")
    public ApiResponse<StudyJournalDto> getStudyJournal(@PathVariable Long journalId) {
        StudyJournalDto journal = studyJournalQueryService.getStudyJournal(journalId);
        return  ApiResponse.onSuccess(journal);
    }

    // 스터디 일지 수정
    @PutMapping("/journal/{journalId}")
    public ApiResponse<StudyJournalDto> updateStudyJournal(
            @PathVariable Long journalId,
            @RequestBody StudyJournalDto studyJournalDto) {
        StudyJournalDto updatedJournal = studyJournalCommandService.updateStudyJournal(journalId, studyJournalDto);
        return  ApiResponse.onSuccess(updatedJournal);
    }

    // 스터디 일지 삭제
    @DeleteMapping("/journal/{journalId}")
    public ApiResponse<Void> deleteStudyJournal(@PathVariable Long journalId) {
        studyJournalCommandService.deleteStudyJournal(journalId);
        return  ApiResponse.onSuccess(null);
    }

    // 세션별 스터디 일지 목록 조회
    @GetMapping("/session/{sessionId}/journals")
    public ApiResponse<List<StudyJournalListDto>> getStudyJournalsBySession(
            @PathVariable Long sessionId,
            @PageableDefault(page = 0, size = 6) Pageable pageable
    ) {
        List<StudyJournalListDto> journals = studyJournalQueryService.getStudyJournalsBySession(sessionId, pageable.getPageNumber(), pageable.getPageSize());
        return  ApiResponse.onSuccess(journals);
    }

    // 스터디일지 이모티콘 토글
    @PatchMapping("/journals/{studyJournalId}/emoji")
    public ApiResponse<StudyJournalDto.EmojiCounts> toggleEmoji(
            @PathVariable Long studyJournalId,
            @AuthenticatedUser User user,
            @RequestBody EmojiToggleRequest request) {

        StudyJournalDto.EmojiCounts emojiCounts = journalEmojiService.toggleEmoji(
                studyJournalId,
                user.getId(),
                request.getEmojiType()
        );

        return  ApiResponse.onSuccess(emojiCounts);
    }
}
