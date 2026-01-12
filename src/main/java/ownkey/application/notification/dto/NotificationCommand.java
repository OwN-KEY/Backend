package ownkey.application.notification.dto;

import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;

public class NotificationCommand {

    public record UpdateSettings(
            Boolean community,
            Boolean wiki,
            Boolean showmethekey
    ) {}

    public record RegisterDevice(
            String deviceToken,
            String environment
    ) {
        public RegisterDevice {
            if (deviceToken == null || deviceToken.isBlank()) {
                throw new BusinessException(ErrorCode.INVALID_INPUT);
            }
        }
    }
}