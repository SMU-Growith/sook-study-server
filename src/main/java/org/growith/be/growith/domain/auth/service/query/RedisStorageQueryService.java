package org.growith.be.growith.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStorageQueryService implements TokenStorageQueryService{

    @Override
    public boolean isBlackList(String token) {
        return false;
    }

    @Override
    public String getRefreshToken(Long id) {
        return "";
    }
}
