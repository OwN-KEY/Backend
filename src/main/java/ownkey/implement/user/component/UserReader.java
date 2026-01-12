package ownkey.implement.user.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.implement.auth.model.KakaoId;
import ownkey.implement.user.model.User;
import ownkey.implement.user.model.vo.Nickname;
import ownkey.infrastructure.user.jpa.UserRepository;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public User get(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public Optional<User> findByKakaoId(KakaoId kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }

    public boolean existsNickname(Nickname nickname) {
        return userRepository.existsByNickname(nickname);
    }
}