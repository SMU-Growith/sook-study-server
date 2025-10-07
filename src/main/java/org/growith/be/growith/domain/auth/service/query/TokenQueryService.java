package org.growith.be.growith.domain.auth.service.query;

import java.time.Duration;

public interface TokenQueryService {
    Long getUserId(String token);
    Duration getAccessTokenExpiration();
    Duration getRefreshTokenExpiration();
}
