package ownkey.implement.auth.model;

import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;

public record KakaoId(String value) {
    public KakaoId {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }
}