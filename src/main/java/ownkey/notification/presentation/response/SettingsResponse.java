package ownkey.notification.presentation.response;

public record SettingsResponse(
        boolean community, boolean wiki, boolean showmethekey
) {}