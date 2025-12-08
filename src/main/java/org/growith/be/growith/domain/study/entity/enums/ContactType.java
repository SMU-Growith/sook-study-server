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
            // DB에 저장된 영문 이름과 API에서 사용하는 한글 description 둘 다 지원
            if (type.name().equals(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ContactType: " + value);
    }
}
