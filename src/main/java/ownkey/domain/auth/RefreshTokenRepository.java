package ownkey.domain.auth;

import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    void deleteByUserId(Long userId);
}