package ownkey.application.auth.port.in;

import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;

public interface KakaoLoginUseCase {
    AuthResult.TokenResponse execute(AuthCommand.KakaoLogin command);
}