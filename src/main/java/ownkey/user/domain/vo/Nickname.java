package ownkey.user.domain.vo;

import ownkey.common.exception.ErrorCode;
import ownkey.common.exception.BusinessException;

public record Nickname(String value) {
    public Nickname {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        String trimmed = value.strip();
        if (trimmed.length() < 2 || trimmed.length() > 8) {
            throw new BusinessException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }
        value = trimmed;
    }
}