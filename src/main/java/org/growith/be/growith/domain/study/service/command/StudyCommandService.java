package org.growith.be.growith.domain.study.service.command;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;

public interface StudyCommandService {
    StudyResponseDto.StudyDetail createStudy(StudyRequestDto.CreateStudyDTO request, Long userId);
    StudyResponseDto.StudyDetail updateStudy(StudyRequestDto.UpdateStudyDTO request, Long studyId, Long userId);
    void deleteStudy(Long studyId, Long userId);
    StudyStatus changeStudyStatus(Long studyId, StudyStatus studyStatus, Long userId);
    StudyStatus toggleStudyStatus(Long studyId, Long userId);
    Boolean toggleIsRecruiting(Long studyId, Long userId);
    void withdrawStudy(Long studyId, Long userId);
    void updateStudyRules(Long studyId, Long userId, StudyRequestDto.UpdateRuleContentDTO request);
    void changeStudyLeader(Long studyId, Long currentLeaderId, Long newLeaderId);
}
