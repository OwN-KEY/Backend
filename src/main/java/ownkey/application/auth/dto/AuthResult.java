package ownkey.application.auth.dto;

public class AuthResult {
    public record TokenResponse(String accessToken, String refreshToken) {}
}
