package ownkey.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "권한이 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다."),
    KAKAO_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "A004", "카카오 API 호출 중 오류가 발생했습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U002", "이미 사용 중인 닉네임입니다."),
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "U003", "닉네임은 2자 이상 8자 이하이어야 합니다.");

    private final HttpStatusCode status;
    private final String code;
    private final String message;
}