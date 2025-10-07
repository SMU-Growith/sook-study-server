package org.growith.be.growith.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.growith.be.growith.global.data.JwtConfigData;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    @Getter
    private final Duration accessExpiration;
    @Getter
    private final Duration refreshExpiration;

    public JwtUtil(JwtConfigData jwtConfigData){
        this.secretKey = Keys.hmacShaKeyFor(jwtConfigData.getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofMillis(jwtConfigData.getTime().getAccessToken());
        this.refreshExpiration = Duration.ofMillis(jwtConfigData.getTime().getRefreshToken());
    }

    public String createAccessToken(CustomUserDetails details) {
        return createToken(details, accessExpiration);
    }

    public String createRefreshToken(CustomUserDetails details) {
        return createToken(details, refreshExpiration);
    }

    public Long getUserId(String token){
        try {
            return getClaims(token).getPayload().get("id", Long.class);
        } catch (ExpiredJwtException e) {
            // TODO: TokenException 던지기
//            throw new TokenException();
            return null;
        } catch (JwtException e) {
            return null;
        }
    }

    public String createToken(CustomUserDetails detail, Duration expiration){
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(detail.getUsername())
                .claim("id", detail.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token);
    }

}
