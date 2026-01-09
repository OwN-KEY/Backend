package ownkey.application.user.port.in;

import ownkey.application.user.dto.UserCommand;

public interface UpdateProfileUseCase {
    void execute(Long userId, UserCommand.UpdateProfile command);
}