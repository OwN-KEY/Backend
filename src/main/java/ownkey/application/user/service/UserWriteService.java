package ownkey.application.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.user.dto.UserCommand;
import ownkey.application.user.port.in.UpdateProfileUseCase;
import ownkey.application.user.port.in.WithdrawUseCase;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.domain.auth.RefreshToken;
import ownkey.domain.auth.RefreshTokenRepository;
import ownkey.domain.user.User;
import ownkey.domain.user.UserRepository;
import ownkey.infrastructure.auth.kakao.KakaoApiClient;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserWriteService implements UpdateProfileUseCase, WithdrawUseCase {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoApiClient kakaoApiClient;

    @Value("${kakao.admin-key}")
    private String adminKey;

    @Override
    public void execute(Long userId, UserCommand.UpdateProfile command) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (command.nickname() != null && !command.nickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(command.nickname())) {
                throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
            }
        }

        user.updateProfile(command.nickname(), command.profileImageId(), command.backgroundImageId());
    }

    @Override
    public void execute(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        try {
            kakaoApiClient.unlink(
                    "KakaoAK " + adminKey,
                    "user_id",
                    user.getKakaoId()
            );
        } catch (Exception e) {
            log.error("Kakao unlink failed for userId: {}", userId, e);
            throw new BusinessException(ErrorCode.KAKAO_API_ERROR);
        }

        refreshTokenRepository.deleteByUserId(userId);
        user.withdraw();
    }
}