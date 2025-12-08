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
            // DB에 저장된 영문 이름과 API에서 사용하는 한글 description 둘 다 지원
            if (category.name().equals(value) || category.getDescription().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown StudyStyleCategory: " + value);
    }
}
