package ownkey.auth.application.command;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record KakaoLoginCommand(String idToken) {
    public KakaoLoginCommand {
        if (idToken == null || idToken.isBlank())
            throw new BusinessException(ErrorCode.INVALID_INPUT);
    }
}
