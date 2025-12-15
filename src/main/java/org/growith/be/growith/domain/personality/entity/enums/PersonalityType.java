package org.growith.be.growith.domain.personality.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PersonalityType {
    PLANNED("계획형"),
    FREE("자유형"),
    COOPERATIVE("협력형"),
    ACHIEVEMENT("실적형");

    private final String description;
}
