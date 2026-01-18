package ownkey.common.response;

import ownkey.common.exception.ErrorCode;

public record ErrorResponse(ErrorDetails error) {
    public record ErrorDetails(String code, String message) {}

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(new ErrorDetails(errorCode.getCode(), errorCode.getMessage()));
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(new ErrorDetails(code, message));
    }
}