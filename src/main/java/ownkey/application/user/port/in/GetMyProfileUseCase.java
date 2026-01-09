package ownkey.application.user.port.in;

import ownkey.application.user.dto.UserResult;

public interface GetMyProfileUseCase {
    UserResult.MyProfile execute(Long userId);
}