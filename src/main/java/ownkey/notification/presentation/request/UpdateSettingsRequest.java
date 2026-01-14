package ownkey.notification.presentation.request;

public record UpdateSettingsRequest(
        Boolean community, Boolean wiki, Boolean showmethekey
) {}