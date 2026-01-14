package ownkey.auth.application.command;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record LogoutCommand(String accessToken, String refreshToken) {
    public LogoutCommand {
        if (accessToken == null || accessToken.isBlank())
            throw new BusinessException(ErrorCode.INVALID_INPUT);
    }
}
