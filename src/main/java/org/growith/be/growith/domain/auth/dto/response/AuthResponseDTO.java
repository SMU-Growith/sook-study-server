package org.growith.be.growith.domain.auth.dto.response;

import lombok.Builder;

public record AuthResponseDTO() {

    @Builder
    public record TokenResult(
            String accessToken,
            String refreshToken
    ){}

    @Builder
    public record LoginResult(
            Long userId,
            String accessToken
    ){}


}
