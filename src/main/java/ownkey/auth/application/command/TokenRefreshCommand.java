package ownkey.auth.application.command;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record TokenRefreshCommand(String refreshToken) {
    public TokenRefreshCommand {
        if (refreshToken == null || refreshToken.isBlank())
            throw new BusinessException(ErrorCode.INVALID_INPUT);
    }
}
