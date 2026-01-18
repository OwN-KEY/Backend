package ownkey.auth.domain;

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
    private String token;

    @Indexed
    private Long userId;

    @TimeToLive
    private long ttl;

    public static RefreshToken of(String token, Long userId, long ttl) {
        return new RefreshToken(token, userId, ttl);
    }
}