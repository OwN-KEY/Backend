package ownkey.implement.auth.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.implement.auth.model.RefreshToken;
import ownkey.infrastructure.auth.redis.RefreshTokenRepository;

@Component
@RequiredArgsConstructor
public class TokenWriter {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken token) {
        refreshTokenRepository.save(token);
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public void deleteById(String tokenValue) {
        refreshTokenRepository.deleteById(tokenValue);
    }
}