package org.growith.be.growith.domain.journal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.*;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
import org.growith.be.growith.domain.journal.service.command.StudyJournalCommandService;
import org.growith.be.growith.domain.journal.service.query.StudyJournalQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
@Tag(name = "스터디 세션 일지 API")
public class StudySessionController {

    private final StudyJournalCommandService studyJournalCommandService;
    private final StudyJournalQueryService studyJournalQueryService;
    private final JournalEmojiService journalEmojiService;


    //스터디 세션 목록 조회
    @Operation(
            summary = "스터디 세션 목록 조회 API",
            description = "스터디 세션 목록을 조회할 수 있습니다."
    )
    @GetMapping("/{studyId}/sessions")
    public ApiResponse<StudySessionListDto> getStudySessions(
            @PathVariable Long studyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        StudySessionListDto sessions = studyJournalQueryService.getStudySessions(studyId, page, size);
        return  ApiResponse.onSuccess(sessions);
    }

    // 스터디 세션 생성 (팀장권한)
    @Operation(
            summary = "스터디 세션 생성 API",
            description = "팀장 권한을 가진 사용자만 스터디 세션을 생성할 수 있습니다."
    )
    @PostMapping("/{studyId}/session")
    public ApiResponse<StudySessionCardDto> createStudySession(
            @PathVariable Long studyId,
            @RequestBody CreateStudySessionRequest request) {
        StudySessionCardDto createdSession = studyJournalCommandService.createStudySession(studyId, request);
        return  ApiResponse.onSuccess(createdSession);
    }

    // 스터디 세션 수정 (팀장권한)
    @Operation(
            summary = "스터디 세션 수정 API",
            description = "팀장 권한을 가진 사용자만 스터디 세션을 수정할 수 있습니다."
    )
    @PutMapping("/session/{sessionId}")
    public ApiResponse<Void> updateStudySession(
            @PathVariable Long sessionId,
            @RequestBody UpdateStudySessionRequest request) {
        studyJournalCommandService.updateStudySession(sessionId, request);
        return  ApiResponse.onSuccess(null);
    }

    // 스터디 세션 상세 조회
    @Operation(
            summary = "스터디 세션 상세 조회 API",
            description = "스터디 세션을 상세 조회할 수 있습니다."
    )
    @GetMapping("/session/{sessionId}")
    public ApiResponse<StudySessionCardDto> getStudySession(
            @PathVariable Long sessionId
    ) {
        StudySessionCardDto studySession = studyJournalQueryService.getStudySessionById(sessionId);
        return  ApiResponse.onSuccess(studySession);
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

    // 스터디 일지 제출(생성)
    @Operation(
            summary = "스터디 일지 제출(생성) API",
            description = "스터디 일지를 제출(생성)할 수 있습니다."
    )
    @PostMapping("/session/{sessionId}/journal")
    public ApiResponse<StudyJournalDto> createStudyJournal(
            @PathVariable Long sessionId,
            @AuthenticatedUser User user,
            @RequestBody CreateStudyJournalRequest request) {
        StudyJournalDto createdJournal = studyJournalCommandService.createStudyJournal(sessionId, user.getId(), request);
        return  ApiResponse.onSuccess(createdJournal);
    }

    // 스터디 일지 상세 조회
    @Operation(
            summary = "스터디 일지 상세 조회 API",
            description = "스터디 일지를 상세 조회할 수 있습니다."
    )
    @GetMapping("/journal/{journalId}")
    public ApiResponse<StudyJournalDto> getStudyJournal(
            @AuthenticatedUser User user,
            @PathVariable Long journalId
    ) {
        StudyJournalDto journal = studyJournalQueryService.getStudyJournal(journalId, user.getId());
        return  ApiResponse.onSuccess(journal);
    }

    // 스터디 일지 수정
    @Operation(
            summary = "스터디 일지 수정 API",
            description = "스터디 일지를 수정할 수 있습니다."
    )
    @PutMapping("/journal/{journalId}")
    public ApiResponse<StudyJournalDto> updateStudyJournal(
            @PathVariable Long journalId,
            @RequestBody UpdateStudyJournalRequest request) {
        StudyJournalDto updatedJournal = studyJournalCommandService.updateStudyJournal(journalId, request);
        return  ApiResponse.onSuccess(updatedJournal);
    }

    // 스터디 일지 삭제
    @Operation(
            summary = "스터디 일지 삭제 API",
            description = "스터디 일지를 삭제할 수 있습니다."
    )
    @DeleteMapping("/journal/{journalId}")
    public ApiResponse<Void> deleteStudyJournal(@PathVariable Long journalId) {
        studyJournalCommandService.deleteStudyJournal(journalId);
        return  ApiResponse.onSuccess(null);
    }

    // 세션별 일지 목록 조회
    @Operation(
            summary = "세션별 일지 목록 조회 API",
            description = "세션별 일지 목록을 조회할 수 있습니다."
    )
    @GetMapping("/session/{sessionId}/journals")
    public ApiResponse<StudyJournalListResponse> getStudyJournalsBySession(
            @PathVariable Long sessionId,
            @AuthenticatedUser User user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        StudyJournalListResponse response = studyJournalQueryService.getStudyJournalsBySession(
                sessionId, 
                user.getId(), 
                pageable.getPageNumber(), 
                pageable.getPageSize()
        );
        return  ApiResponse.onSuccess(response);
    }

    // 스터디일지 이모티콘 토글
    @Operation(
            summary = "스터디일지 이모티콘 토글 API",
            description = "스터디일지 이모티콘을 토글할 수 있습니다."
    )
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
