package ownkey.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.auth.application.command.KakaoLoginCommand;
import ownkey.auth.application.command.LogoutCommand;
import ownkey.auth.application.command.TokenRefreshCommand;
import ownkey.auth.application.result.AuthTokenResult;
import ownkey.auth.domain.OidcProfile;
import ownkey.auth.implement.*;
import ownkey.auth.application.usecase.KakaoLoginUseCase;
import ownkey.auth.application.usecase.LogoutUseCase;
import ownkey.auth.application.usecase.TokenRefreshUseCase;
import ownkey.user.implement.UserReader;
import ownkey.user.implement.UserWriter;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.auth.domain.AuthToken;
import ownkey.auth.domain.RefreshToken;
import ownkey.user.domain.User;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements KakaoLoginUseCase, TokenRefreshUseCase, LogoutUseCase {

    private final IdTokenValidator idTokenValidator;
    private final AuthTokenManager authTokenManager;
    private final RefreshTokenReader refreshTokenReader;
    private final TokenWriter tokenWriter;
    private final TokenBlacklistManager tokenBlacklistManager;

    private final UserReader userReader;
    private final UserWriter userWriter;

    @Value("${jwt.refresh-token-expiration-sec}")
    private long refreshTokenTtlSeconds;

    @Override
    public AuthTokenResult login(KakaoLoginCommand command) {
        AuthToken idToken = new AuthToken(command.idToken());
        OidcProfile profile = idTokenValidator.validate(idToken);

        User user = userReader.findByKakaoId(profile.kakaoId())
                .orElseGet(() -> userWriter.save(new User(
                        profile.kakaoId(),
                        profile.nickname(),
                        profile.profileImage()
                )));

        return createAndSaveTokens(user);
    }

    @Override
    public AuthTokenResult refresh(TokenRefreshCommand command) {
        AuthToken refreshTokenVo = new AuthToken(command.refreshToken());

        if (!authTokenManager.validateToken(refreshTokenVo.value())) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        RefreshToken storedToken = refreshTokenReader.get(refreshTokenVo.value());

        User user = userReader.get(storedToken.getUserId());

        tokenWriter.delete(storedToken);
        return createAndSaveTokens(user);
    }

    @Override
    public void logout(LogoutCommand command) {
        AuthToken accessToken = new AuthToken(command.accessToken());
        AuthToken refreshToken = (command.refreshToken() != null)
                ? new AuthToken(command.refreshToken())
                : null;

        if (!authTokenManager.validateToken(accessToken.value())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        if (refreshToken != null) {
            tokenWriter.deleteById(refreshToken.value());
        }

        long remaining = authTokenManager.getRemainingMilliSeconds(accessToken.value());
        if (remaining > 0) {
            tokenBlacklistManager.blacklist(accessToken.value(), remaining);
        }
    }

    private AuthTokenResult createAndSaveTokens(User user) {
        String access = authTokenManager.createAccessToken(user.getId(), user.getRole().name());
        String refresh = authTokenManager.createRefreshToken(user.getId());

        tokenWriter.save(RefreshToken.of(refresh, user.getId(), refreshTokenTtlSeconds));

        return new AuthTokenResult(access, refresh);
    }
}