package ownkey.application.notification.port.in;

import ownkey.application.notification.dto.NotificationCommand;

public interface ManageDeviceTokenUseCase {
    void register(Long userId, NotificationCommand.RegisterDevice command);
    void unregister(Long userId, Long deviceId);
}