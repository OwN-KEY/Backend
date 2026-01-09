package ownkey.application.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;
import ownkey.application.auth.port.in.KakaoLoginUseCase;
import ownkey.application.auth.port.in.LogoutUseCase;
import ownkey.application.auth.port.in.TokenRefreshUseCase;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.common.security.jwt.JwtTokenProvider;
import ownkey.domain.auth.RefreshToken;
import ownkey.domain.auth.RefreshTokenRepository;
import ownkey.domain.auth.TokenBlacklistRepository;
import ownkey.domain.user.User;
import ownkey.domain.user.UserRepository;
import ownkey.infrastructure.auth.kakao.IdTokenValidator;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthWriteService implements KakaoLoginUseCase, TokenRefreshUseCase, LogoutUseCase {

    private final IdTokenValidator idTokenValidator;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResult.TokenResponse execute(AuthCommand.KakaoLogin command) {

        Claims claims = idTokenValidator.validateAndGetClaims(command.idToken());

        String kakaoId = claims.getSubject();
        String nickname = claims.get("nickname", String.class);
        String profileImage = claims.get("picture", String.class);

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> userRepository.save(new User(kakaoId, nickname, profileImage)));

        return createAndSaveTokens(user);
    }

    @Override
    public AuthResult.TokenResponse execute(AuthCommand.Refresh command) {

        if (!tokenProvider.validateToken(command.refreshToken())) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        RefreshToken storedToken = refreshTokenRepository.findById(command.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.EXPIRED_TOKEN));

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        refreshTokenRepository.delete(storedToken);
        return createAndSaveTokens(user);
    }

    @Override
    public void execute(AuthCommand.Logout command) {

        if (!tokenProvider.validateToken(command.accessToken())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        if (command.refreshToken() != null) {
            refreshTokenRepository.deleteById(command.refreshToken());
        }

        long remainingTime = tokenProvider.getRemainingMilliSeconds(command.accessToken());
        if (remainingTime > 0) {
            tokenBlacklistRepository.blacklist(command.accessToken(), remainingTime);
        }
    }

    private AuthResult.TokenResponse createAndSaveTokens(User user) {
        String access = tokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String refresh = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(RefreshToken.of(refresh, user.getId(), 1209600L));

        return new AuthResult.TokenResponse(access, refresh);
    }
}