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
        for (StudyFormat format : StudyFormat.values()) {
            if (format.getDescription().equals(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown StudyFormat: " + value);
    }
}
