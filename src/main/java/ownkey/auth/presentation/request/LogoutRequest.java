package ownkey.auth.presentation.request;

public record LogoutRequest(
        String refreshToken
) {}