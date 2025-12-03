package org.growith.be.growith.domain.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.service.command.StudyCommandService;
import org.growith.be.growith.domain.study.service.query.StudyQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
@Tag(name = "스터디 API")
public class StudyController {
    private final StudyCommandService studycommandService;
    private final StudyQueryService studyQueryService;

    @GetMapping
    public ApiResponse<List<StudyCardDto>> getStudies(
            @RequestParam(required = false) List<String> fields,
            @RequestParam(required = false) List<String> formats,
            @RequestParam(required = false) List<String> styles,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "new") String sort,
            @PageableDefault(page = 0, size = 12) Pageable pageable
    ) {
        return ApiResponse.onSuccess(null
//                studyQueryService.searchStudies(fields, formats, styles, status, keyword, sort, page, size)
        );
    }

    @GetMapping("/myStudies")
    public ApiResponse<List<StudyResponseDto.StudyCardDto>> getMyStudies(
            @AuthenticatedUser User user,
            @RequestParam(defaultValue = "ACTIVE") String studyStatus,
            @PageableDefault(page = 0, size = 6) Pageable pageable
    ) {
//        studyQueryService.getMyStudies(String.valueOf(user.getUserId()), page, size, studyStatus)
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/popular")
    public ApiResponse<List<StudyCardDto>> getPopularStudies(
            @PageableDefault(page = 0, size = 4) Pageable pageable
    ) {
//        studyQueryService.getPopularStudies(page, size);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/new")
    public ApiResponse<List<StudyCardDto>> getNewStudies(
            @PageableDefault(page = 0, size = 4) Pageable pageable
    ) {
//        studyQueryService.getNewStudies(page, size);
        return  ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "스터디 생성 API"
            , description = "스터디 생성 API, 스터디를 생성한 사용자는 팀장 권한을 갖는다"
    )
    @PostMapping
    public ApiResponse<StudyResponseDto.StudyDetail> createStudy(
            @RequestBody @Valid StudyRequestDto.CreateStudyDTO request,
            @AuthenticatedUser User user
    ) {
        StudyResponseDto.StudyDetail studyDetailDto = studycommandService.createStudy(request, user.getId());
        return  ApiResponse.onSuccess(studyDetailDto);
    }

    @Operation(
            summary = "스터디 상세 조회 API"
            , description = "스터디의 상세 조회 API"
    )
    @GetMapping("/{studyId}")
    public ApiResponse<StudyResponseDto.StudyDetail> getStudyDetail(@PathVariable Long studyId) {
        return ApiResponse.onSuccess(studyQueryService.getStudyDetail(studyId));
    }

    @Operation(
            summary = "스터디 수정 API"
            , description = "스터디 수정 API,  팀장 권한을 갖는 사용자만 스터디를 수정할 수 있다"
    )
    @PutMapping("/{studyId}")
    public ApiResponse<StudyResponseDto.StudyDetail> updateStudy(
            @PathVariable Long studyId,
            @RequestBody StudyRequestDto.UpdateStudyDTO request,
            @AuthenticatedUser User user
    ) {
        StudyResponseDto.StudyDetail studyDetail = studycommandService.updateStudy(request, studyId, user.getId());
        return  ApiResponse.onSuccess(studyDetail);
    }

    @Operation(
            summary = "스터디 상태 수정 API"
            , description = "스터디 상태 수정 API,  팀장 권한을 갖는 사용자만 스터디 상태를 수정할 수 있다"
    )
    @PatchMapping("/{studyId}/change-status")
    public ApiResponse<StudyResponseDto.ChangedStudyStatus> closedStudy(
            @RequestBody StudyRequestDto.ChangeStudyStatusDTO request,
            @PathVariable Long studyId,
            @AuthenticatedUser User user
    ) {
        StudyStatus studyStatus = studycommandService.changeStudyStatus(studyId, request.studyStatus(), user.getId());
        return ApiResponse.onSuccess(StudyConverter.toChangedStudyStatus(studyStatus));
    }
    
    @Operation(
            summary = "스터디 삭제 API"
            , description = "스터디 삭제 API,  팀장 권한을 갖는 사용자만 스터디를 수정할 수 있다"
    )
    @DeleteMapping("/{studyId}")
    public ApiResponse<Void> deleteStudy(@PathVariable Long studyId,@AuthenticatedUser User user) {
        studycommandService.deleteStudy(studyId, user.getId());
        return  ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/{studyId}/withdraw")
    public ApiResponse<Void> withdrawStudy(
            @PathVariable Long studyId,
            @AuthenticatedUser User user
    ) {
//        studycommandService.withdrawStudy(studyId, String.valueOf(user.getUserId()));
        return  ApiResponse.onSuccess(null);
    }


    // 스터디 멤버 조회
    @GetMapping("/{studyId}/users")
    public ApiResponse<List<Void>> getStudyMembers(@PathVariable Long studyId) {
//        return ResponseEntity.ok(studyQueryService.getStudyMembers(studyId));
        return  ApiResponse.onSuccess(null);
    }

    // 스터디 팀장 변경
    @PatchMapping("/{studyId}/changeLeader")
    public ApiResponse<Void> changeStudyLeader(
            @PathVariable Long studyId,
            @AuthenticatedUser User user,
            @RequestParam Long newLeaderUserId) {
//        studyCommandService.changeStudyLeader(studyId, user.getId(), newLeaderUserId);
        return  ApiResponse.onSuccess(null);
    }

   
}
