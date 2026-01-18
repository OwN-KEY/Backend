package ownkey.auth.domain;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record AuthToken(String value) {
    public AuthToken {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }
}