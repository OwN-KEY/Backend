package ownkey.auth.infrastructure.redis;

import org.springframework.data.repository.CrudRepository;
import ownkey.auth.domain.RefreshToken;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    void deleteByUserId(Long userId);
}