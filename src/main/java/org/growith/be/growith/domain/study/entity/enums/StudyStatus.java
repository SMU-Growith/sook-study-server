package org.growith.be.growith.domain.study.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StudyStatus {
    ACTIVE, CLOSED;

    public StudyStatus toggle() {
        return this == ACTIVE ? CLOSED : ACTIVE;
    }

    @JsonCreator
    public static StudyStatus from(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        for (StudyStatus status : StudyStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
