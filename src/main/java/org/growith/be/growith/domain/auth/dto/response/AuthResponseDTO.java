package org.growith.be.growith.domain.auth.dto.response;

import lombok.Builder;

public record AuthResponseDTO() {

    @Builder
    public record Login(
            String accessToken,
            String refreshToken
    ){}
}
