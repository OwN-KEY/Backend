package ownkey.auth.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.auth.domain.RefreshToken;
import ownkey.auth.infrastructure.redis.RefreshTokenRepository;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class RefreshTokenReader {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken get(String tokenValue) {
        return refreshTokenRepository.findById(tokenValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXPIRED_TOKEN));
    }
}