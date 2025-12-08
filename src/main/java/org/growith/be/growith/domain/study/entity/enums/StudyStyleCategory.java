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
        for (StudyStyleCategory category : StudyStyleCategory.values()) {
            if (category.getDescription().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown StudyStyleCategory: " + value);
    }
}
