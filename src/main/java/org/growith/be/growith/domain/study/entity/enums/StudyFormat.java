package org.growith.be.growith.domain.study.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyFormat {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    HYBRID("온라인/오프라인");

    @JsonValue
    private final String description;

    @JsonCreator
    public static StudyFormat from(String value) {
        if (value == null) return null;
        for (StudyFormat format : StudyFormat.values()) {
            // 한글 description과 영문 이름 둘 다 허용 (제약 없음)
            if (format.name().equalsIgnoreCase(value) || format.getDescription().equals(value)) {
                return format;
            }
        }
        // 매칭되지 않으면 null 반환 (에러 발생 안 함)
        return null;
    }
}
