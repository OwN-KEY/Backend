package ownkey.implement.auth.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.infrastructure.common.security.jwt.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class AuthTokenManager {

    private final JwtTokenProvider jwtTokenProvider;

    public String createAccessToken(Long userId, String role) {
        return jwtTokenProvider.createAccessToken(userId, role);
    }

    public String createRefreshToken(Long userId) {
        return jwtTokenProvider.createRefreshToken(userId);
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public long getRemainingMilliSeconds(String token) {
        return jwtTokenProvider.getRemainingMilliSeconds(token);
    }
}