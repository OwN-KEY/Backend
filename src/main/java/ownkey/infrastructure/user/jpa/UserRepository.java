package ownkey.infrastructure.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ownkey.implement.auth.model.KakaoId;
import ownkey.implement.user.model.User;
import ownkey.implement.user.model.vo.Nickname;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(KakaoId kakaoId);

    boolean existsByNickname(Nickname nickname);
}