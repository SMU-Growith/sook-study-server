package org.growith.be.growith.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.scrap.repository.StudyScrapRepository;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final StudyScrapRepository studyScrapRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void createScrap(Long studyId, User user) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if (studyScrapRepository.findByUserAndStudy(user, study).isPresent()) {
            throw new IllegalStateException("이미 스크랩한 스터디입니다.");
        }
        StudyScrap scrap = new StudyScrap(user, study);
        studyScrapRepository.save(scrap);
        study.increaseScrapCount();
    }

    @Transactional
    public void deleteScrap(Long studyScrapId, User user) {
        StudyScrap scrap = studyScrapRepository.findById(studyScrapId)
                .orElseThrow(() -> new IllegalArgumentException("스크랩이 존재하지 않습니다."));
        if (!scrap.getUser().equals(user)) {
            throw new IllegalStateException("본인만 삭제할 수 있습니다.");
        }
        Study study = scrap.getStudy();
        studyScrapRepository.delete(scrap);
        study.decreaseScrapCount();
    }

    @Transactional(readOnly = true)
    public Page<StudyScrap> getUserScraps(User user, int page, int size) {
        return studyScrapRepository.findByUser(user, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public long countUserScraps(User user) {
        return studyScrapRepository.countByUser(user);
    }
}
