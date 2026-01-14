package ownkey.notification.application.command;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record RegisterDeviceCommand(
        String deviceToken,
        String environment
) {
    public RegisterDeviceCommand {
        if (deviceToken == null || deviceToken.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }
}
