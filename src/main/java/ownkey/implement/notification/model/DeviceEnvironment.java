package ownkey.implement.notification.model;

public enum DeviceEnvironment {
    SANDBOX, PRODUCTION;

    public static DeviceEnvironment from(String value) {
        if (value == null || value.isBlank()) return PRODUCTION;
        try {
            return DeviceEnvironment.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PRODUCTION;
        }
    }
}