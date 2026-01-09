package ownkey.application.user.dto;

import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

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