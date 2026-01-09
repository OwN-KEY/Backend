package ownkey.application.auth.port.in;

import ownkey.application.auth.dto.AuthCommand;

public interface LogoutUseCase {
    void execute(AuthCommand.Logout command);
}