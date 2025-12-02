package org.growith.be.growith.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.converter.AuthConverter;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.growith.be.growith.global.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenCommandService implements TokenCommandService {

    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO.TokenResult createLoginToken(CustomUserDetails customUserDetails) {
        return AuthConverter.toTokenResult(
                jwtUtil.createAccessToken(customUserDetails),
                jwtUtil.createRefreshToken(customUserDetails)
        );
    }

    @Override
    public String reissueAccessToken(CustomUserDetails customUserDetails) {
        return jwtUtil.createAccessToken(customUserDetails);
    }
}
