package ownkey.user.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ownkey.auth.domain.KakaoId;
import ownkey.user.domain.User;
import ownkey.user.domain.vo.Nickname;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(KakaoId kakaoId);

    boolean existsByNickname(Nickname nickname);
}