package ownkey.user.application.usecase;

import ownkey.user.application.command.UpdateProfileCommand;

public interface UpdateProfileUseCase {
    void updateProfile(Long userId, UpdateProfileCommand command);
}