package ownkey.user.presentation.request;

public record UpdateProfileRequest(
        String nickname,
        String profileImageId,
        String backgroundImageId
) {}