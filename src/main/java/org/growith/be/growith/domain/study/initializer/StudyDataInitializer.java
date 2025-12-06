package org.growith.be.growith.domain.study.initializer;

import lombok.RequiredArgsConstructor;

import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.ContactType;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.entity.enums.StudyFormat;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
/**
 * 애플리케이션 시작 시 더미 스터디 데이터를 3개 삽입합니다.
 * <p>
 * 실제 운영 환경에서는 {@code spring.profiles.active=prod} 등으로
 * 프로파일을 구분해 비활성화할 수 있습니다.
 */
@Component
@RequiredArgsConstructor
public class StudyDataInitializer implements CommandLineRunner {

    private final StudyRepository studyRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 이미 데이터가 있으면 삽입을 건너뜁니다.
        if (studyRepository.count() > 0) {
            return;
        }

        // ── 더미 스터디 1 ────────────────────────────────────────
        Study study1 = Study.builder()
                .title("AI 기반 스타트업 아이디어톤")
                .description("AI 기술을 활용한 새로운 비즈니스 모델을 구상하고, 팀원을 모집합니다.")
                .studyStatus(StudyStatus.ACTIVE)
                .contactType(ContactType.KAKAO)
                .url("https://example.com/ai-idea")
                .isRecruiting(true)
                .studyFormat(StudyFormat.ONLINE)
                .build();

        // ── 더미 스터디 2 ────────────────────────────────────────
        Study study2 = Study.builder()
                .title("프론트엔드 모임 - React & Next.js")
                .description("React와 Next.js를 활용한 최신 웹 개발 트렌드를 공유합니다.")
                .studyStatus(StudyStatus.ACTIVE)
                .contactType(ContactType.EMAIL)
                .url("https://example.com/frontend-meetup")
                .isRecruiting(true)
                .studyFormat(StudyFormat.OFFLINE)
                .build();

        // ── 더미 스터디 3 ────────────────────────────────────────
        Study study3 = Study.builder()
                .title("데이터 사이언스 스터디")
                .description("Python, Pandas, Scikit‑Learn을 이용한 데이터 분석 실습")
                .studyStatus(StudyStatus.ACTIVE)
                .contactType(ContactType.EMAIL)
                .url("https://example.com/data-science")
                .isRecruiting(false)   // 모집 종료 예시
                .studyFormat(StudyFormat.ONLINE)
                .build();

        // 저장
        studyRepository.save(study1);
        studyRepository.save(study2);
        studyRepository.save(study3);
    }
}
