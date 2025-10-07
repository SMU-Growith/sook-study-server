package org.growith.be.growith.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.global.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtTokenQueryService implements TokenQueryService{

    private final JwtUtil jwtUtil;

    @Override
    public Long getUserId(String token) {
        return jwtUtil.getUserId(token);
    }

    @Override
    public Duration getAccessTokenExpiration() {
        return jwtUtil.getAccessExpiration();
    }

    @Override
    public Duration getRefreshTokenExpiration() {
        return jwtUtil.getRefreshExpiration();
    }
}
