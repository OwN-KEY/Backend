package ownkey.auth.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class TokenBlacklistRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void blacklist(String accessToken, long remainingMilliSeconds) {
        redisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "logout",
                Duration.ofMillis(remainingMilliSeconds)
        );
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }
}