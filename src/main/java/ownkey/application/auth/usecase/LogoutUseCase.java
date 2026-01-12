package ownkey.application.auth.usecase;

import ownkey.application.auth.dto.AuthCommand;

public interface LogoutUseCase {
    void logout(AuthCommand.Logout command);
}