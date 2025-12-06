package org.growith.be.growith.domain.user.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Major {
    COMPUTER("컴퓨터"),
    DESIGN("디자인");

    private final String description;
}
