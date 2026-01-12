package ownkey.implement.auth.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.infrastructure.auth.redis.TokenBlacklistRepository;

@Component
@RequiredArgsConstructor
public class TokenBlacklistManager {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public void blacklist(String accessToken, long remainingMilliSeconds) {
        tokenBlacklistRepository.blacklist(accessToken, remainingMilliSeconds);
    }
}