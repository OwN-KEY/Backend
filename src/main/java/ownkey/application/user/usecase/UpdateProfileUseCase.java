package ownkey.application.user.usecase;

import ownkey.application.user.dto.UserCommand;

public interface UpdateProfileUseCase {
    void updateProfile(Long userId, UserCommand.UpdateProfile command);
}