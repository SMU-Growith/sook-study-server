package org.growith.be.growith.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.constants.TokenStorageConstants;
import org.growith.be.growith.global.util.RedisUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStorageQueryService implements TokenStorageQueryService{

    private final RedisUtil redisUtil;

    @Override
    public boolean isBlackList(String token) {
        return false;
    }

    @Override
    public String getRefreshToken(Long id) {
        return redisUtil.get(TokenStorageConstants.REFRESH_TOKEN_PREFIX + id, String.class);
    }
}
