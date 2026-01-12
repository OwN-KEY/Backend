package ownkey.application.auth.usecase;

import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;

public interface KakaoLoginUseCase {
    AuthResult.TokenResponse login(AuthCommand.KakaoLogin command);
}