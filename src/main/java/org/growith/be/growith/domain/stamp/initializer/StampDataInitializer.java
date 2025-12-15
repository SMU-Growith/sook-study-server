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

        // 웰컴숙 (레벨 없음)
        stamps.add(createStampWithoutLevel(StampType.WELCOME, "웰컴숙",
                "숙터디 회원가입을 축하해요! 숙터디에서 다양한 활동을 이용해보세요."));

        // 리더숙
        stamps.add(createStamp(StampType.LEADER, StampLevel.LEVEL_1, "조교 송이", "스터디 1회 개설",
                "스터디 개설을 하셨네요. 스터디장은 스터디 일지를 회차별로 생성할 수 있어요."));
        stamps.add(createStamp(StampType.LEADER, StampLevel.LEVEL_2, "교수 송이", "스터디 3회 개설",
                "스터디 개설을 하셨네요. 스터디장은 스터디 일지를 회차별로 생성할 수 있어요."));

        // 기록숙
        stamps.add(createStamp(StampType.RECORD, StampLevel.LEVEL_1, "장학생 송이", "학습일지 3회 작성",
                "학습 일지를 열심히 작성하셨군요! 스터디가 끝나도 일지는 계속 볼 수 있어요."));
        stamps.add(createStamp(StampType.RECORD, StampLevel.LEVEL_2, "수석 송이", "학습일지 10회 작성",
                "학습 일지를 열심히 작성하셨군요! 스터디가 끝나도 일지는 계속 볼 수 있어요."));

        // 응원 고숙
        stamps.add(createStamp(StampType.CHEER, StampLevel.LEVEL_1, "응원단 송이", "3명에게 응원하기",
                "송이들에게 응원하기를 하셨네요! 응원받은 송이에게 힘이 되어 줘요 :)"));
        stamps.add(createStamp(StampType.CHEER, StampLevel.LEVEL_2, "치어리더 송이", "10명에게 응원하기",
                "송이들에게 응원하기를 하셨네요! 응원받은 송이에게 힘이 되어 줘요 :)"));

        // 슈퍼숙타
        stamps.add(createStamp(StampType.SUPERSTAR, StampLevel.LEVEL_1, "새내기 송이", "응원하기 누적 10개",
                "내 스터디 일지에 많은 응원을 받았어요. 어쩌면 스터디 인기쟁이는 나일지도..?"));
        stamps.add(createStamp(StampType.SUPERSTAR, StampLevel.LEVEL_2, "인싸 송이", "응원하기 누적 20개",
                "내 스터디 일지에 많은 응원을 받았어요. 어쩌면 스터디 인기쟁이는 나일지도..?"));

        stampRepository.saveAll(stamps);
        log.info("Stamp data initialization completed. Total stamps: {}", stamps.size());
    }

    private Stamp createStamp(StampType type, StampLevel level, String name, String levelDescription, String description) {
        return Stamp.builder()
                .stampType(type)
                .stampLevel(level)
                .name(name)
                .levelDescription(levelDescription)
                .description(description)
                .build();
    }

    private Stamp createStampWithoutLevel(StampType type, String name, String description) {
        return Stamp.builder()
                .stampType(type)
                .stampLevel(null)
                .name(name)
                .levelDescription(null)
                .description(description)
                .build();
    }
}
