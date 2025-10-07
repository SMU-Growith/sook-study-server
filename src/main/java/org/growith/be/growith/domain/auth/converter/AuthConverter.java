package org.growith.be.growith.domain.auth.converter;

import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;

public class AuthConverter {

    public static AuthResponseDTO.Login toLoginResponse(String accessToken, String refreshToken) {
        return AuthResponseDTO.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
