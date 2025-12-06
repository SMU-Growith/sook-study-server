package org.growith.be.growith.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.scrap.repository.StudyScrapRepository;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScrapService {
    private final StudyScrapRepository studyScrapRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public record ToggleResult(Study study, Boolean isScraped) {}

    public ToggleResult toggleScrap(Long studyId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        Optional<StudyScrap> studyScrap = studyScrapRepository.findByUserAndStudy(user, study);
        boolean isScraped;

        if (studyScrap.isPresent()) {
            studyScrapRepository.delete(studyScrap.get());
            study.decreaseScrapCount();
            isScraped = false;
        } else {
            studyScrapRepository.save(StudyScrap.builder()
                    .user(user).study(study).build());
            study.increaseScrapCount();
            isScraped = true;
        }
        return new ToggleResult(study, isScraped);
    }

    @Transactional(readOnly = true)
    public Page<StudyScrap> getUserScraps(User user, Pageable pageable) {
        return studyScrapRepository.findByUser(user, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }
}
