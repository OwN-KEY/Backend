package ownkey.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.notification.application.command.RegisterDeviceCommand;
import ownkey.notification.application.command.UpdateSettingsCommand;
import ownkey.notification.application.result.SettingsResult;
import ownkey.notification.application.usecase.ManageDeviceTokenUseCase;
import ownkey.notification.application.usecase.ManageNotificationUseCase;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.notification.domain.DeviceToken;
import ownkey.notification.implement.DeviceTokenManager;
import ownkey.notification.implement.NotificationSettingReader;
import ownkey.notification.implement.NotificationSettingWriter;
import ownkey.notification.infrastructure.jpa.DeviceTokenRepository;
import ownkey.notification.domain.NotificationSetting;
import ownkey.notification.infrastructure.jpa.NotificationSettingRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService implements ManageNotificationUseCase, ManageDeviceTokenUseCase {

    private final NotificationSettingReader settingReader;
    private final NotificationSettingWriter settingWriter;
    private final DeviceTokenManager deviceTokenManager;


    @Override
    @Transactional(readOnly = true)
    public SettingsResult getSettings(Long userId) {
        NotificationSetting setting = settingReader.getOrNew(userId);

        return new SettingsResult(
                setting.isCommunity(),
                setting.isWiki(),
                setting.isShowmethekey()
        );
    }

    @Override
    public void updateSettings(Long userId, UpdateSettingsCommand command) {
        NotificationSetting setting = settingReader.getOrNew(userId);

        setting.update(command.community(), command.wiki(), command.showmethekey());
        settingWriter.save(setting);
    }


    @Override
    public void registerDevice(Long userId, RegisterDeviceCommand command) {
        String tokenValue = command.deviceToken();
        var existingOpt = deviceTokenManager.findByToken(tokenValue);

        if (existingOpt.isPresent()) {
            DeviceToken existing = existingOpt.get();
            if (existing.getUserId().equals(userId)) {
                return;
            }
            deviceTokenManager.delete(existing);
        }

        deviceTokenManager.save(new DeviceToken(userId, tokenValue, command.environment()));
    }

    @Override
    public void unregisterDevice(Long userId, Long deviceId) {
        DeviceToken token = deviceTokenManager.get(deviceId);

        if (!token.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        deviceTokenManager.delete(token);
    }
}