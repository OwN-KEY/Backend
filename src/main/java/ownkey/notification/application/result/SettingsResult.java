package ownkey.notification.application.result;

public record SettingsResult(
        boolean community,
        boolean wiki,
        boolean showmethekey
) {
}
