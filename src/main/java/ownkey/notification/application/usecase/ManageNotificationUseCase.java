package ownkey.notification.application.usecase;

import ownkey.notification.application.command.UpdateSettingsCommand;
import ownkey.notification.application.result.SettingsResult;

public interface ManageNotificationUseCase {
    SettingsResult getSettings(Long userId);
    void updateSettings(Long userId, UpdateSettingsCommand command);
}