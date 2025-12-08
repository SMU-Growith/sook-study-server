package org.growith.be.growith.domain.study.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyStyleCategory {
    SYSTEMATIC("체계적인"),
    FREE("자유로운"),
    COOPERATIVE("협력적인"),
    RESULT_ORIENTED("실적중심");

    @JsonValue
    private final String description;

    @JsonCreator
    public static StudyStyleCategory from(String value) {
        if (value == null) return null;
        for (StudyStyleCategory category : StudyStyleCategory.values()) {
            // 한글 description과 영문 이름 둘 다 허용 (제약 없음)
            if (category.name().equalsIgnoreCase(value) || category.getDescription().equals(value)) {
                return category;
            }
        }
        // 매칭되지 않으면 null 반환 (에러 발생 안 함)
        return null;
    }
}
