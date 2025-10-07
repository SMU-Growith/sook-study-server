package org.growith.be.growith.domain.auth.service.query;

public interface TokenStorageQueryService {
    boolean isBlackList(String token);
}
