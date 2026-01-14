package ownkey.user.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.auth.domain.KakaoId;
import ownkey.user.domain.User;
import ownkey.user.domain.vo.Nickname;
import ownkey.user.infrastructure.jpa.UserRepository;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

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