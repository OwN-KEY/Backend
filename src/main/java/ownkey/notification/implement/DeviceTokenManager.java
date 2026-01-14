package ownkey.notification.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.notification.domain.DeviceToken;
import ownkey.notification.infrastructure.jpa.DeviceTokenRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceTokenManager {
    private final DeviceTokenRepository repository;

    public Optional<DeviceToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public void save(DeviceToken deviceToken) {
        repository.save(deviceToken);
    }

    public void delete(DeviceToken deviceToken) {
        repository.delete(deviceToken);
    }

    public DeviceToken get(Long deviceId) {
        return repository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
    }
}