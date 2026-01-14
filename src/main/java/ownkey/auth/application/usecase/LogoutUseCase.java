package ownkey.auth.application.usecase;

import ownkey.auth.application.command.LogoutCommand;

public interface LogoutUseCase {
    void logout(LogoutCommand command);
}