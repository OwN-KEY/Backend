package ownkey.user.application.usecase;

import ownkey.user.application.result.CheckNicknameResult;

public interface CheckNicknameUseCase {
    CheckNicknameResult isNicknameAvailable(String nickname);
}