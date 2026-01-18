package ownkey.notification.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.notification.domain.NotificationSetting;
import ownkey.notification.infrastructure.jpa.NotificationSettingRepository;

@Component
@RequiredArgsConstructor
public class NotificationSettingReader {
    private final NotificationSettingRepository repository;

    public NotificationSetting getOrNew(Long userId) {
        return repository.findById(userId)
                .orElseGet(() -> new NotificationSetting(userId));
    }
}