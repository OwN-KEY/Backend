package ownkey.auth.domain;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record KakaoId(String value) {
    public KakaoId {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }
}