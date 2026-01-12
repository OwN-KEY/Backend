package ownkey.application.user.usecase;

import ownkey.application.user.dto.UserResult;

public interface GetMyProfileUseCase {
    UserResult.MyProfile getProfile(Long userId);
}