package org.growith.be.growith.domain.application.service.query;

import org.growith.be.growith.domain.application.entity.StudyApplication;

import java.util.List;

public interface StudyApplicationQueryService {
    public List<StudyApplication> getStudyApplications(Long studyId);
}
