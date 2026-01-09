package ownkey.application.auth.port.in;

import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;

public interface TokenRefreshUseCase {
    AuthResult.TokenResponse execute(AuthCommand.Refresh command);
}