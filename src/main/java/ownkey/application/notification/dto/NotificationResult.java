package ownkey.application.notification.dto;

public class NotificationResult {
    public record Settings(
            boolean community,
            boolean wiki,
            boolean showmethekey
    ) {}
}