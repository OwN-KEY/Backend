package ownkey.auth.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.auth.infrastructure.redis.TokenBlacklistRepository;

@Component
@RequiredArgsConstructor
public class TokenBlacklistManager {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public void blacklist(String accessToken, long remainingMilliSeconds) {
        tokenBlacklistRepository.blacklist(accessToken, remainingMilliSeconds);
    }
}