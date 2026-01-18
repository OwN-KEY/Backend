package ownkey.notification.application.command;

public record UpdateSettingsCommand(
        Boolean community,
        Boolean wiki,
        Boolean showmethekey
) {
}
