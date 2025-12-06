package org.growith.be.growith.domain.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.application.converter.StudyApplicationConverter;
import org.growith.be.growith.domain.application.dto.request.StudyApplicationRequestDTO;
import org.growith.be.growith.domain.application.dto.response.StudyApplicationResponseDTO;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.application.service.command.StudyApplicationCommandService;
import org.growith.be.growith.domain.application.service.query.StudyApplicationQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
@Tag(name = "스터디 지원서 API")
public class StudyApplicationController {

    private final StudyApplicationCommandService studyApplicationCommandService;
    private final StudyApplicationQueryService studyApplicationQueryService;

    // 스터디 지원서 생성
    @Operation(summary = "스터디 지원서 생성 API", description = "해당 스터디의 스터디 지원서를 생성하는 API")
    @PostMapping("/{studyId}/application")
    public ApiResponse<StudyApplicationResponseDTO.StudyApplicationDetailDTO> createApplication(
            @PathVariable Long studyId,
            @AuthenticatedUser User user,
            @RequestBody StudyApplicationRequestDTO.CreateStudyApplicationDTO applicationDto
    ) {
        StudyApplication studyApplication = studyApplicationCommandService.createApplication(studyId, user.getId(), applicationDto);
        return ApiResponse.onSuccess(StudyApplicationConverter.toApplicationDetailDTO(studyApplication));
    }

    // 지원서 승인/거절
    @Operation(summary = "스터디 지원서 승인/거절 API (팀장 권한)", description = "해당 스터디의 스터디 지원서를 승인/거절하는 API, 팀장 권한")
    @PatchMapping("/{applicationId}/status")
    public ApiResponse<StudyApplicationResponseDTO.UpdateApplicationResponseDTO> updateApplicationStatus(
            @PathVariable Long applicationId,
            @AuthenticatedUser User user,
            @RequestBody StudyApplicationRequestDTO.UpdateStudyApplicationDTO request
    ) {
        StudyApplication studyApplication = studyApplicationCommandService.updateApplicationStatus(applicationId, user.getId(), request);
        return ApiResponse.onSuccess(StudyApplicationConverter.toUpdateApplicationResponseDTO(studyApplication));
    }

    // 지원서 리스트 조회
    @Operation(summary = "스터디 지원서 리스트 조회 API", description = "해당 스터디의 스터디 지원서 리스트를 조회하는 API")
    @GetMapping("/{studyId}/applications")
    public ApiResponse<List<StudyApplicationResponseDTO.StudyApplicationDetailDTO>> getApplications(@PathVariable Long studyId) {
        List<StudyApplication> studyApplications = studyApplicationQueryService.getStudyApplications(studyId);
        return ApiResponse.onSuccess(StudyApplicationConverter.toApplicationDetailDTOList(studyApplications));
    }
}
