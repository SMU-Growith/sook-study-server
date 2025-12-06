package org.growith.be.growith.domain.study.initializer;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 더미 일지(StudyJournal) 데이터를 3개 삽입합니다.
 * StudyDataInitializer 에서 만든 스터디가 존재한다면 해당 스터디와 연관된 세션 ID를 지정해도 좋습니다.
 * 여기서는 간단히 userId 와 sessionId 를 null 로 두어 외래키 제약을 회피합니다.
 */
@Component
@RequiredArgsConstructor
public class StudyJournalDataInitializer implements CommandLineRunner {

    private final StudyJournalRepository studyJournalRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (studyJournalRepository.count() > 0) {
            return;
        }

        // ── 더미 일지 1 ────────────────────────────────────────
        StudyJournal journal1 = StudyJournal.createJournal(
                "첫 번째 일지",
                "AI 스타트업 아이디어톤 진행 상황을 기록합니다.",
                "https://example.com/journal1",
                null, // sessionId (없음)
                null  // userId (없음)
        );

        // ── 더미 일지 2 ────────────────────────────────────────
        StudyJournal journal2 = StudyJournal.createJournal(
                "두 번째 일지",
                "React & Next.js 스터디 회고 및 학습 내용 정리",
                "https://example.com/journal2",
                null, // sessionId (없음)
                null  // userId (없음)
        );

        // ── 더미 일지 3 ────────────────────────────────────────
        StudyJournal journal3 = StudyJournal.createJournal(
                "세 번째 일지",
                "데이터 사이언스 실습 결과와 코드 스니펫 공유",
                "https://example.com/journal3",
                null, // sessionId (없음)
                null  // userId (없음)
        );

        studyJournalRepository.save(journal1);
        studyJournalRepository.save(journal2);
        studyJournalRepository.save(journal3);
    }
}
