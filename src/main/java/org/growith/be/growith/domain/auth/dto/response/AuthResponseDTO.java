package org.growith.be.growith.domain.auth.dto.response;

import lombok.Builder;

public record AuthResponseDTO() {

    @Builder
    public record TokenResult(
            Long userId,
            String nickName,
            String email,
            String major,
            String studentStatus,
            String phoneNumber,
            String accessToken,
            String refreshToken
    ){}

    @Builder
    public record AccessTokenResult(
            Long userId,
            String accessToken
    ){}


}
