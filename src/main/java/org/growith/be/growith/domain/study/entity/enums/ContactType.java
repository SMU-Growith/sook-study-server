package org.growith.be.growith.domain.study.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContactType {
    KAKAO("카카오톡"),
    EMAIL("이메일");

    @JsonValue
    private final String description;

    @JsonCreator
    public static ContactType from(String value) {
        for (ContactType type : ContactType.values()) {
            if (type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ContactType: " + value);
    }
}
