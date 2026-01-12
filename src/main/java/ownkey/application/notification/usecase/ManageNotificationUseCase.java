package ownkey.application.notification.usecase;

import ownkey.application.notification.dto.NotificationCommand;
import ownkey.application.notification.dto.NotificationResult;

public interface ManageNotificationUseCase {
    NotificationResult.Settings getSettings(Long userId);
    void updateSettings(Long userId, NotificationCommand.UpdateSettings command);
}