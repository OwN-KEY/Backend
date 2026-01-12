package ownkey.application.auth.usecase;

import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;

public interface TokenRefreshUseCase {
    AuthResult.TokenResponse refresh(AuthCommand.Refresh command);
}