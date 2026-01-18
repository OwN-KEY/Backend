package ownkey.notification.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.notification.domain.NotificationSetting;
import ownkey.notification.infrastructure.jpa.NotificationSettingRepository;

@Component
@RequiredArgsConstructor
public class NotificationSettingWriter {
    private final NotificationSettingRepository repository;

    public NotificationSetting save(NotificationSetting setting) {
        return repository.save(setting);
    }
}