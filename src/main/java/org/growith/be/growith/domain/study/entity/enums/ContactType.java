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
        if (value == null) return null;
        for (ContactType type : ContactType.values()) {
            // 한글 description과 영문 이름 둘 다 허용 (제약 없음)
            if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        // 매칭되지 않으면 null 반환 (에러 발생 안 함)
        return null;
    }
}
