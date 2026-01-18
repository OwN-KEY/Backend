package ownkey.auth.application.usecase;

import ownkey.auth.application.command.KakaoLoginCommand;
import ownkey.auth.application.result.AuthTokenResult;

public interface KakaoLoginUseCase {
    AuthTokenResult login(KakaoLoginCommand command);
}