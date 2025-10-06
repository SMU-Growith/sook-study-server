package org.growith.be.growith.global.util;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.entity.Style;
import org.growith.be.growith.domain.study.repository.StudyFieldRepository;
import org.growith.be.growith.domain.study.repository.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final StyleRepository styleRepository;
    private final StudyFieldRepository studyFieldRepository;

    @Bean
    @Transactional
    public CommandLineRunner initFixedData() {
        return args -> {
            // 1. Style 고정값 insert
            if (styleRepository.count() == 0) {
                List<String> styleNames = Arrays.asList("체계적인", "자유로운", "협력적인", "실적중심");
                for (String name : styleNames) {
                    styleRepository.save(Style.builder().styleName(name).build());
                }
            }

            // 2. StudyField 고정값 insert (상위/하위)
            if (studyFieldRepository.count() == 0) {
                // 상위 카테고리
                StudyField academic = studyFieldRepository.save(StudyField.builder().name("학업").level(1).sort_order(1).build());
                StudyField language = studyFieldRepository.save(StudyField.builder().name("언어").level(1).sort_order(2).build());
                StudyField career = studyFieldRepository.save(StudyField.builder().name("취업/커리어").level(1).sort_order(3).build());
                StudyField selfdev = studyFieldRepository.save(StudyField.builder().name("자기계발").level(1).sort_order(4).build());

                // 하위 카테고리 - 학업
                studyFieldRepository.save(StudyField.builder().name("전공 공부").parent(academic).level(2).sort_order(1).build());
                studyFieldRepository.save(StudyField.builder().name("시험 공부").parent(academic).level(2).sort_order(2).build());
                studyFieldRepository.save(StudyField.builder().name("자격증").parent(academic).level(2).sort_order(3).build());
                studyFieldRepository.save(StudyField.builder().name("고시·임용·공무원").parent(academic).level(2).sort_order(4).build());

                // 하위 카테고리 - 언어
                studyFieldRepository.save(StudyField.builder().name("회화").parent(language).level(2).sort_order(1).build());
                studyFieldRepository.save(StudyField.builder().name("외국어 시험").parent(language).level(2).sort_order(2).build());

                // 하위 카테고리 - 취업/커리어
                studyFieldRepository.save(StudyField.builder().name("면접·자소서").parent(career).level(2).sort_order(1).build());
                studyFieldRepository.save(StudyField.builder().name("디자인").parent(career).level(2).sort_order(2).build());
                studyFieldRepository.save(StudyField.builder().name("IT").parent(career).level(2).sort_order(3).build());
                studyFieldRepository.save(StudyField.builder().name("마케팅").parent(career).level(2).sort_order(4).build());
                studyFieldRepository.save(StudyField.builder().name("코딩").parent(career).level(2).sort_order(5).build());
                studyFieldRepository.save(StudyField.builder().name("데이터 분석").parent(career).level(2).sort_order(6).build());

                // 하위 카테고리 - 자기계발
                studyFieldRepository.save(StudyField.builder().name("독서·글쓰기").parent(selfdev).level(2).sort_order(1).build());
                studyFieldRepository.save(StudyField.builder().name("운동").parent(selfdev).level(2).sort_order(2).build());
                studyFieldRepository.save(StudyField.builder().name("사진·영상").parent(selfdev).level(2).sort_order(3).build());
            }
        };
    }
}

