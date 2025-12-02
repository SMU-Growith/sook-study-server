package org.growith.be.growith.domain.auth.service.command;


import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.constants.TokenStorageConstants;
import org.growith.be.growith.domain.auth.service.query.TokenQueryService;
import org.growith.be.growith.global.util.RedisUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStorageCommandService implements TokenStorageCommandService{

    private final RedisUtil redisUtil;
    private final TokenQueryService tokenQueryService;

    @Override
    public void addBlackList(String token) {
        redisUtil.set(TokenStorageConstants.BLACKLIST_PREFIX + token, true, tokenQueryService.getRefreshTokenExpiration());
    }

    @Override
    public void addRefreshToken(Long id, String refresh) {
        redisUtil.set(TokenStorageConstants.REFRESH_TOKEN_PREFIX + id, refresh, tokenQueryService.getRefreshTokenExpiration());
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        redisUtil.delete(TokenStorageConstants.REFRESH_TOKEN_PREFIX + userId);
    }
}
