package ownkey.application.user.dto;

import java.time.LocalDateTime;

public class UserResult {

    public record MyProfile(
            Long id,
            String nickname,
            String profileImageId,
            String backgroundImageId,
            String role,
            LocalDateTime createdAt
    ) {}

    public record ActivitySummary(
            Long id,
            String title,
            String contentPreview,
            LocalDateTime createdAt
    ) {}
}