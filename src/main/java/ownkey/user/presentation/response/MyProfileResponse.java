package ownkey.user.presentation.response;
import ownkey.user.application.result.MyProfileResult;

import java.time.LocalDateTime;

public record MyProfileResponse(
        Long id,
        String nickname,
        String profileImageId,
        String backgroundImageId,
        String role,
        String status,
        LocalDateTime createdAt
) {
    public static MyProfileResponse from(MyProfileResult result) {
        return new MyProfileResponse(
                result.id(),
                result.nickname(),
                result.profileImageId(),
                result.backgroundImageId(),
                result.role(),
                result.status(),
                result.createdAt()
        );
    }
}