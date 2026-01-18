package ownkey.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret-key:default_secret_key_must_be_longer_than_32_bytes_for_hmac_sha}")
    private String secretKeyString;

    @Value("${jwt.access-token-expiration-sec:600}")
    private long accessTokenExpirationSec;

    @Value("${jwt.refresh-token-expiration-sec:31536000}")
    private long refreshTokenExpirationSec;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userId, String role) {
        return createToken(userId, role, accessTokenExpirationSec);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, null, refreshTokenExpirationSec);
    }

    private String createToken(Long userId, String role, long expirationSec) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + (expirationSec * 1000));

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(validity)
                .signWith(key);

        if (role != null) {
            builder.claim("role", role);
        }
        return builder.compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.parseLong(parseClaims(token).getPayload().getSubject());
    }

    public long getRemainingMilliSeconds(String token) {
        Date expiration = parseClaims(token).getPayload().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}