package ownkey.application.auth.dto;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public class AuthCommand {

    public record KakaoLogin(String idToken) {
        public KakaoLogin {
            if (idToken == null || idToken.isBlank())
                throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    public record Refresh(String refreshToken) {
        public Refresh {
            if (refreshToken == null || refreshToken.isBlank())
                throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    public record Logout(String accessToken, String refreshToken) {
        public Logout {
            if (accessToken == null || accessToken.isBlank())
                throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }
}