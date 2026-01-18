package ownkey.user.application.usecase;

import ownkey.user.application.result.MyProfileResult;

public interface GetMyProfileUseCase {
    MyProfileResult getProfile(Long userId);
}