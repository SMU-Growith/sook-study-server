package org.growith.be.growith.domain.application.service.command;

import org.growith.be.growith.domain.application.dto.request.StudyApplicationRequestDTO;
import org.growith.be.growith.domain.application.entity.StudyApplication;

public interface StudyApplicationCommandService {
    StudyApplication createApplication(Long studyId, Long userId, StudyApplicationRequestDTO.CreateStudyApplicationDTO request);
    StudyApplication updateApplicationStatus(Long applicationId, Long userId, StudyApplicationRequestDTO.UpdateStudyApplicationDTO request);
}
