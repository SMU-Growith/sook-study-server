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
            if (category.getDescription().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown RuleCategory: " + value);
    }
}
