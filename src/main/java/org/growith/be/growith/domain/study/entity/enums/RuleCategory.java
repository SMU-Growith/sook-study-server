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
        if (value == null) return null;
        for (RuleCategory category : RuleCategory.values()) {
            // 한글 description과 영문 이름 둘 다 허용 (제약 없음)
            if (category.name().equalsIgnoreCase(value) || category.getDescription().equals(value)) {
                return category;
            }
        }
        // 매칭되지 않으면 null 반환 (에러 발생 안 함)
        return null;
    }
}
