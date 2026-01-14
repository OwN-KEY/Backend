package ownkey.auth.application.usecase;

import ownkey.auth.application.command.TokenRefreshCommand;
import ownkey.auth.application.result.AuthTokenResult;

public interface TokenRefreshUseCase {
    AuthTokenResult refresh(TokenRefreshCommand command);
}