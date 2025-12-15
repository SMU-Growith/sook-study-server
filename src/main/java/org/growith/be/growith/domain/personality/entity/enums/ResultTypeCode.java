package org.growith.be.growith.domain.personality.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultTypeCode {
    // 단일 유형
    METICULOUS("꼼꼼송이", "계획형"),
    FLUTTERING("팔랑송이", "자유형"),
    WARM("도란송이", "협력형"),
    BRILLIANT("번쩍송이", "실적형"),
    
    // 혼합 유형
    MYSTERIOUS("알쏭송이", "계획/자유 혼합형"),
    RELIABLE("든든송이", "계획/협력 혼합형"),
    METICULOUS_STRICT("깐깐송이", "계획/실적 혼합형"),
    CHEERFUL("방긋송이", "자유/협력 혼합형"),
    SPARKLING("톡톡송이", "자유/실적 혼합형"),
    PROUD("으쓱송이", "협력/실적 혼합형"),
    
    // 균형형
    ALLROUNDER("올라운더송이", "균형형");

    private final String typeName;
    private final String typeCategory;
}
