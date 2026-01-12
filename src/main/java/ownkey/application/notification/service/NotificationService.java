package ownkey.application.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.notification.dto.NotificationCommand;
import ownkey.application.notification.dto.NotificationResult;
import ownkey.application.notification.usecase.ManageDeviceTokenUseCase;
import ownkey.application.notification.usecase.ManageNotificationUseCase;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;
import ownkey.implement.notification.model.DeviceToken;
import ownkey.infrastructure.notification.jpa.DeviceTokenRepository;
import ownkey.implement.notification.model.NotificationSetting;
import ownkey.infrastructure.notification.jpa.NotificationSettingRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService implements ManageNotificationUseCase, ManageDeviceTokenUseCase {

    private final NotificationSettingRepository settingRepository;
    private final DeviceTokenRepository deviceTokenRepository;


    @Override
    @Transactional(readOnly = true)
    public NotificationResult.Settings getSettings(Long userId) {
        NotificationSetting setting = settingRepository.findById(userId)
                .orElseGet(() -> new NotificationSetting(userId));

        return new NotificationResult.Settings(
                setting.isCommunity(),
                setting.isWiki(),
                setting.isShowmethekey()
        );
    }

    @Override
    public void updateSettings(Long userId, NotificationCommand.UpdateSettings command) {
        NotificationSetting setting = settingRepository.findById(userId)
                .orElseGet(() -> settingRepository.save(new NotificationSetting(userId)));

        setting.update(command.community(), command.wiki(), command.showmethekey());
    }


    @Override
    public void registerDevice(Long userId, NotificationCommand.RegisterDevice command) {
        String tokenValue = command.deviceToken();
        var existingOpt = deviceTokenRepository.findByToken(tokenValue);

        if (existingOpt.isPresent()) {
            DeviceToken existing = existingOpt.get();
            if (existing.getUserId().equals(userId)) {
                return;
            }
            deviceTokenRepository.delete(existing);
        }

        deviceTokenRepository.save(new DeviceToken(userId, tokenValue, command.environment()));
    }

    @Override
    public void unregisterDevice(Long userId, Long deviceId) {
        DeviceToken token = deviceTokenRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));

        if (!token.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        deviceTokenRepository.delete(token);
    }
}