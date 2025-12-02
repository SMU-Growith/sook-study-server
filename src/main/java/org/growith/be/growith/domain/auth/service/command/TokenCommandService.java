package org.growith.be.growith.domain.auth.service.command;

import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
import org.growith.be.growith.global.security.domain.CustomUserDetails;

public interface TokenCommandService {
    AuthResponseDTO.TokenResult createLoginToken(CustomUserDetails customUserDetails);
    String reissueAccessToken(CustomUserDetails customUserDetails);
}
