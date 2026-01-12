package ownkey.implement.auth.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.implement.auth.model.RefreshToken;
import ownkey.infrastructure.auth.redis.RefreshTokenRepository;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class RefreshTokenReader {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken get(String tokenValue) {
        return refreshTokenRepository.findById(tokenValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXPIRED_TOKEN));
    }
}