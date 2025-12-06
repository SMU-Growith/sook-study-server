package org.growith.be.growith.domain.study.entity.enums;

public enum StudyStatus {
    ACTIVE, CLOSED;

    public StudyStatus toggle() {
        return this == ACTIVE ? CLOSED : ACTIVE;
    }
}
