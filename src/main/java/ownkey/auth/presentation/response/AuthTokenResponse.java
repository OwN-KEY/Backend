package ownkey.auth.presentation.response;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken
) {}