package ownkey.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id
    private String refreshToken;

    @Indexed
    private Long userId;

    @TimeToLive
    private long ttl;

    public static RefreshToken of(String refreshToken, Long userId, long ttl) {
        return new RefreshToken(refreshToken, userId, ttl);
    }
}