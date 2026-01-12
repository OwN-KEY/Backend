package ownkey.application.user.dto;

import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;

public class UserCommand {
    public record UpdateProfile(String nickname, String profileImageId, String backgroundImageId) {
        public UpdateProfile {
            nickname = (nickname != null) ? nickname.strip() : null;
            if (nickname != null && (nickname.length() < 2 || nickname.length() > 8)) {
                throw new BusinessException(ErrorCode.INVALID_NICKNAME_LENGTH);
            }
        }
    }
}