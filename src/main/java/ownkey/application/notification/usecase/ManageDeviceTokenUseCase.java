package ownkey.application.notification.usecase;

import ownkey.application.notification.dto.NotificationCommand;

public interface ManageDeviceTokenUseCase {
    void registerDevice(Long userId, NotificationCommand.RegisterDevice command);
    void unregisterDevice(Long userId, Long deviceId);
}