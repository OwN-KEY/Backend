package ownkey.notification.application.usecase;

import ownkey.notification.application.command.RegisterDeviceCommand;

public interface ManageDeviceTokenUseCase {
    void registerDevice(Long userId, RegisterDeviceCommand command);
    void unregisterDevice(Long userId, Long deviceId);
}