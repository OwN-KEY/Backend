package ownkey.user.application.result;

import java.time.LocalDateTime;

public record MyProfileResult(
        Long id,
        String nickname,
        String profileImageId,
        String backgroundImageId,
        String role,
        String status,
        LocalDateTime createdAt
) {
}
