package ownkey.infrastructure.auth.redis;

import org.springframework.data.repository.CrudRepository;
import ownkey.implement.auth.model.RefreshToken;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    void deleteByUserId(Long userId);
}