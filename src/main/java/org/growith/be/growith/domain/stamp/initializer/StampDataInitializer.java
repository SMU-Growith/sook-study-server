package org.growith.be.growith.domain.stamp.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.stamp.entity.Stamp;
import org.growith.be.growith.domain.stamp.entity.enums.StampLevel;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;
import org.growith.be.growith.domain.stamp.repository.StampRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StampDataInitializer implements CommandLineRunner {

    private final StampRepository stampRepository;

    @Override
    public void run(String... args) {
        if (stampRepository.count() > 0) {
            log.info("Stamp data already exists. Skipping initialization.");
            return;
        }

        log.info("Initializing stamp data...");

        List<Stamp> stamps = new ArrayList<>();

        // 웰컴숙
        stamps.add(createStamp(StampType.WELCOME, StampLevel.LEVEL_1, "웰컴숙", "회원가입 완료"));

        // 리더숙
        stamps.add(createStamp(StampType.LEADER, StampLevel.LEVEL_1, "조교 송이", "스터디 1회 개설"));
        stamps.add(createStamp(StampType.LEADER, StampLevel.LEVEL_2, "교수 송이", "스터디 3회 개설"));

        // 기록숙
        stamps.add(createStamp(StampType.RECORD, StampLevel.LEVEL_1, "장학생 송이", "학습일지 3회 작성"));
        stamps.add(createStamp(StampType.RECORD, StampLevel.LEVEL_2, "수석 송이", "학습일지 10회 작성"));

        // 응원 고숙
        stamps.add(createStamp(StampType.CHEER, StampLevel.LEVEL_1, "응원단 송이", "3명에게 응원하기"));
        stamps.add(createStamp(StampType.CHEER, StampLevel.LEVEL_2, "치어리더 송이", "10명에게 응원하기"));

        // 슈퍼숙타
        stamps.add(createStamp(StampType.SUPERSTAR, StampLevel.LEVEL_1, "새내기 송이", "응원하기 누적 10개"));
        stamps.add(createStamp(StampType.SUPERSTAR, StampLevel.LEVEL_2, "인싸 송이", "응원하기 누적 20개"));

        stampRepository.saveAll(stamps);
        log.info("Stamp data initialization completed. Total stamps: {}", stamps.size());
    }

    private Stamp createStamp(StampType type, StampLevel level, String name, String description) {
        return Stamp.builder()
                .stampType(type)
                .stampLevel(level)
                .name(name)
                .description(description)
                .build();
    }
}
