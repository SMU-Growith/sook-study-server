package org.growith.be.growith.domain.auth.service.command;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStorageCommandService implements TokenStorageCommandService{

    @Override
    public void addBlackList(String token) {
        // TODO: Redis 설정 코드 필요
    }

    @Override
    public void addRefreshToken(Long id, String refreshToken) {

    }

    @Override
    public void deleteRefreshToken(Long userId) {

    }
}
