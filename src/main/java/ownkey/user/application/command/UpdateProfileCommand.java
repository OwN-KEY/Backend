package ownkey.user.application.command;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

public record UpdateProfileCommand(String nickname, String profileImageId, String backgroundImageId) {
    public UpdateProfileCommand {
        nickname = (nickname != null) ? nickname.strip() : null;
        if (nickname != null && (nickname.length() < 2 || nickname.length() > 8)) {
            throw new BusinessException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }
    }
}
