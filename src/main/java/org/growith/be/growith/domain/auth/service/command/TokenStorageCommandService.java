package org.growith.be.growith.domain.auth.service.command;

public interface TokenStorageCommandService {
    void addRefreshToken(Long id, String refreshToken);
    void addBlackList(String token);
    void deleteRefreshToken(Long userId);
}
