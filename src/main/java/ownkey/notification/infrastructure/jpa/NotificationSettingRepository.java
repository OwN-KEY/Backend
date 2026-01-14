package ownkey.notification.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ownkey.notification.domain.NotificationSetting;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}