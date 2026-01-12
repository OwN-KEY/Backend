package ownkey.infrastructure.notification.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ownkey.implement.notification.model.NotificationSetting;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}