package org.growith.be.growith.domain.application.service.query;

import org.growith.be.growith.domain.application.dto.response.StudyApplicationResponseDTO;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.user.entity.User;

import java.util.List;

public interface StudyApplicationQueryService {
    List<StudyApplication> getStudyApplications(Long studyId, User user);
    List<StudyApplicationResponseDTO.MyApplicationCardDTO> getMyApplications(User user);
}
