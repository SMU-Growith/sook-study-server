package org.growith.be.growith.domain.application.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.application.repository.StudyApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyApplicationQueryServiceImpl implements StudyApplicationQueryService{

    private final StudyApplicationRepository studyApplicationRepository;

    // 지원서 조회
    public List<StudyApplication> getStudyApplications(Long studyId) {
        return studyApplicationRepository.findByStudyId(studyId);
    }
}
