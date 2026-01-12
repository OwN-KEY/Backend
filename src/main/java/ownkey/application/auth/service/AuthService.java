package ownkey.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;
import ownkey.application.auth.dto.OidcProfile;
import ownkey.application.auth.usecase.KakaoLoginUseCase;
import ownkey.application.auth.usecase.LogoutUseCase;
import ownkey.application.auth.usecase.TokenRefreshUseCase;
import ownkey.implement.auth.component.*;
import ownkey.implement.user.component.UserReader;
import ownkey.implement.user.component.UserWriter;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;
import ownkey.implement.auth.model.AuthToken;
import ownkey.implement.auth.model.RefreshToken;
import ownkey.implement.user.model.User;

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
    public AuthResult.TokenResponse login(AuthCommand.KakaoLogin command) {
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
    public AuthResult.TokenResponse refresh(AuthCommand.Refresh command) {
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
    public void logout(AuthCommand.Logout command) {
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

    private AuthResult.TokenResponse createAndSaveTokens(User user) {
        String access = authTokenManager.createAccessToken(user.getId(), user.getRole().name());
        String refresh = authTokenManager.createRefreshToken(user.getId());

        tokenWriter.save(RefreshToken.of(refresh, user.getId(), refreshTokenTtlSeconds));

        return new AuthResult.TokenResponse(access, refresh);
    }
}