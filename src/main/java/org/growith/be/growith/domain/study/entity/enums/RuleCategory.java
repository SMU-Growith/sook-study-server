package org.growith.be.growith.domain.study.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RuleCategory {
    TIME("시간"),
    FINE("벌금"),
    DAY_OFF("휴무"),
    ATMOSPHERE("분위기"),
    ETC("기타");

    @JsonValue
    private final String description;

    @JsonCreator
    public static RuleCategory from(String value) {
        for (RuleCategory category : RuleCategory.values()) {
            // DB에 저장된 영문 이름과 API에서 사용하는 한글 description 둘 다 지원
            if (category.name().equals(value) || category.getDescription().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown RuleCategory: " + value);
    }
}
