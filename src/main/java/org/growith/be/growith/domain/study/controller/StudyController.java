package org.growith.be.growith.domain.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.RuleCategory;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.service.command.StudyCommandService;
import org.growith.be.growith.domain.study.service.query.StudyQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Operation(summary = "스터디 검색 API by 윶", description = "태그를 통해 스터디를 검색하는 API")
    @PostMapping("/search")
    public ApiResponse<StudyResponseDto.StudyPreviewDTOList> getStudies(
            @RequestBody StudyRequestDto.SearchStudyCondition request,
            @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Study> studies = studyQueryService.searchStudies(request, pageable);
        StudyResponseDto.StudyPreviewDTOList studyPreviewDTOList = StudyConverter.toStudyPreviewDTOList(studies);
        return ApiResponse.onSuccess(studyPreviewDTOList);
    }

    @Operation(summary = "내 스터디 리스트 조회 API by 윶", description = "사용자의 참여 이력이 있는 스터디를 리스트로 조회하는 API")
    @PostMapping("/my-studies")
    public ApiResponse<List<StudyResponseDto.UserStudyPreviewDto>> getMyStudies(
            @AuthenticatedUser User user,
            @RequestBody StudyRequestDto.MyStudiesRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        StudyStatus studyStatus = request.studyStatus() != null ? request.studyStatus() : StudyStatus.ACTIVE;
        // sort 파라미터는 무시하고 기본 정렬(createdAt DESC) 사용
        Pageable pageable = PageRequest.of(page, size);
        List<StudyResponseDto.UserStudyPreviewDto> myStudies = studyQueryService.getMyStudies(user.getId(), studyStatus, pageable);
        return ApiResponse.onSuccess(myStudies);
    }

    @Operation(summary = "인기/최근 스터디 조회 API by 윶", description = "홈에서 인기 혹은 최근 스터디 조회하는 API")
    @GetMapping
    public ApiResponse<StudyResponseDto.StudyPreviewDTOList> getPopularStudies(
            @PageableDefault(page = 0, size = 3) Pageable pageable
    ) {
        List<Study> studies = studyQueryService.getStudiesByPopularOrNew(pageable);
        StudyResponseDto.StudyPreviewDTOList studyPreviewDTOList = StudyConverter.toStudyPreviewDTOList(studies);
        return ApiResponse.onSuccess(studyPreviewDTOList);
    }

    @Operation(
            summary = "스터디 생성 API by 윶"
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
            summary = "스터디 상세 조회 API by 윶"
            , description = "스터디의 상세 조회 API"
    )
    @GetMapping("/{studyId}")
    public ApiResponse<StudyResponseDto.StudyDetail> getStudyDetail(@PathVariable Long studyId, @AuthenticatedUser User user) {
        return ApiResponse.onSuccess(studyQueryService.getStudyDetail(studyId, user.getId()));
    }

    @Operation(
            summary = "스터디 상태 토글 API by 서연"
            , description = "스터디 상태를 ACTIVE ↔ CLOSED로 토글. 팀장 권한을 갖는 사용자만 실행할 수 있다."
    )
    @PatchMapping("/{studyId}/toggle-status")
    public ApiResponse<StudyResponseDto.ChangedStudyStatus> toggleStudyStatus(
            @PathVariable Long studyId,
            @AuthenticatedUser User user
    ) {
        StudyStatus newStatus = studycommandService.toggleStudyStatus(studyId, user.getId());
        return ApiResponse.onSuccess(StudyConverter.toChangedStudyStatus(newStatus));
    }

    @Operation(
            summary = "스터디 수정 API by 윶"
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

//     @Operation(
//             summary = "스터디 상태 수정 API"
//             , description = "스터디 상태 수정 API,  팀장 권한을 갖는 사용자만 스터디 상태를 수정할 수 있다"
//     )
//     @PatchMapping("/{studyId}/change-status")
//     public ApiResponse<StudyResponseDto.ChangedStudyStatus> closedStudy(
//             @RequestBody StudyRequestDto.ChangeStudyStatusDTO request,
//             @PathVariable Long studyId,
//             @AuthenticatedUser User user
//     ) {
//         StudyStatus studyStatus = studycommandService.changeStudyStatus(studyId, request.studyStatus(), user.getId());
//         return ApiResponse.onSuccess(StudyConverter.toChangedStudyStatus(studyStatus));
//     }
    
    @Operation(
            summary = "스터디 삭제 API by 윶"
            , description = "스터디 삭제 API,  팀장 권한을 갖는 사용자만 스터디를 수정할 수 있다"
    )
    @DeleteMapping("/{studyId}")
    public ApiResponse<Void> deleteStudy(@PathVariable Long studyId,@AuthenticatedUser User user) {
        studycommandService.deleteStudy(studyId, user.getId());
        return  ApiResponse.onSuccess(null);
    }

    // 스터디 나가기 - 팀원만 나갈 수 있다, 스터디 탈퇴
    @Operation(summary = "스터디 탈퇴 API by 윶", description = "스터디를 탈퇴하는 API, 팀원만 나갈 수 있다")
    @DeleteMapping("/{studyId}/withdraw")
    public ApiResponse<Void> withdrawStudy(
            @PathVariable Long studyId,
            @AuthenticatedUser User user
    ) {
        studycommandService.withdrawStudy(studyId, user.getId());
        return  ApiResponse.onSuccess(null);
    }

    // 규칙 조회
    @Operation(summary = "스터디 규칙 조회 API", description = "스터디 규칙을 조회하는 API")
    @GetMapping("/{studyId}/rules")
    public ApiResponse<List<StudyResponseDto.RuleDetailDTO>> getStudyRules(@PathVariable Long studyId) {
        return ApiResponse.onSuccess(studyQueryService.getStudyRules(studyId));
    }

    // 규칙 수정
    @Operation(summary = "스터디 규칙 수정 API", description = "스터디 규칙을 수정하는 API, 팀장 권한을 갖는 사용자만 스터디 규칙을 수정할 수 있다")
    @PutMapping("/{studyId}/rules")
    public ApiResponse<Void> updateRules(
            @PathVariable Long studyId,
            @RequestBody StudyRequestDto.UpdateRuleContentDTO request,
            @AuthenticatedUser User user
    ) {
        studycommandService.updateStudyRules(studyId, user.getId(), request);
        return ApiResponse.onSuccess(null);
    }

//     @Operation(summary = "스터디 탈퇴 API by 윶", description = "스터디를 탈퇴하는 API, 팀원만 나갈 수 있다")
//     @PutMapping("/{studyId}/rules")
//     public ApiResponse<Void> updateRule(
//             @PathVariable Long studyId,
//             @AuthenticatedUser User user
//     ) {
//         studycommandService.withdrawStudy(studyId, user.getId());
//         return  ApiResponse.onSuccess(null);
//     }
    
    

    // 스터디 멤버 조회
    @Operation(summary = "스터디 멤버 조회 API by 윶", description = "스터디에 참여한 사용자들을 조회하는 API")
    @GetMapping("/{studyId}/users")
    public ApiResponse<List<StudyResponseDto.StudyUsers>> getStudyMembers(@PathVariable Long studyId) {
        List<StudyResponseDto.StudyUsers> studyMembers = studyQueryService.getStudyMembers(studyId);
        return  ApiResponse.onSuccess(studyMembers);
    }

    // 스터디 팀장 변경
    @Operation(summary = "스터디 팀장 변경 API", description = "스터디 팀장을 변경하는 API. 현재 리더만 사용 가능하며, 선택된 멤버와 역할을 교환합니다.")
    @PatchMapping("/{studyId}/changeLeader")
    public ApiResponse<Void> changeStudyLeader(
            @PathVariable Long studyId,
            @AuthenticatedUser User user,
            @RequestParam Long newLeaderUserId) {
        studycommandService.changeStudyLeader(studyId, user.getId(), newLeaderUserId);
        return  ApiResponse.onSuccess(null);
    }

   
    // 스터디 분야 조회
    @Operation(summary = "스터디 분야 목록 조회 API by 서연", description = "스터디 생성 시 선택할 수 있는 분야 목록을 조회합니다.")
    @GetMapping("/fields")
    public ApiResponse<List<StudyResponseDto.StudyFieldDto>> getStudyFields() {
        return ApiResponse.onSuccess(studyQueryService.getStudyFields());
    }
}
