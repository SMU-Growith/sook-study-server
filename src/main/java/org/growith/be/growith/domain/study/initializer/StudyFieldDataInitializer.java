package org.growith.be.growith.domain.study.initializer;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.repository.StudyFieldRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1) // Run before StudyDataInitializer if order matters, though currently it doesn't seem to depend on it.
public class StudyFieldDataInitializer implements CommandLineRunner {

    private final StudyFieldRepository studyFieldRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (studyFieldRepository.count() > 0) {
            return;
        }

        // 1. 학업
        StudyField academic = createCategory("학업");
        createDetails(academic, List.of("전공 공부", "시험 공부", "자격증", "고시·임용·공무원"));

        // 2. 언어
        StudyField language = createCategory("언어");
        createDetails(language, List.of("회화", "외국어 시험"));

        // 3. 취업/커리어
        StudyField career = createCategory("취업/커리어");
        createDetails(career, List.of("면접·자소서", "디자인", "IT", "마케팅", "코딩", "데이터 분석"));

        // 4. 자기계발
        StudyField selfDev = createCategory("자기계발");
        createDetails(selfDev, List.of("독서·글쓰기", "운동", "사진·영상"));
    }

    private StudyField createCategory(String name) {
        return studyFieldRepository.save(StudyField.builder()
                .name(name)
                .level(1)
                .parent(null)
                .build());
    }

    private void createDetails(StudyField parent, List<String> names) {
        for (String name : names) {
            studyFieldRepository.save(StudyField.builder()
                    .name(name)
                    .level(2)
                    .parent(parent)
                    .build());
        }
    }
}
