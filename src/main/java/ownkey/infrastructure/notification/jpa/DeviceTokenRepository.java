package ownkey.infrastructure.notification.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ownkey.implement.notification.model.DeviceToken;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByToken(String token);
}