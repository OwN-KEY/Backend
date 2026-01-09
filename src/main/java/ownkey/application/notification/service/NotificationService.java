package ownkey.application.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.notification.dto.NotificationCommand;
import ownkey.application.notification.dto.NotificationResult;
import ownkey.application.notification.port.in.ManageDeviceTokenUseCase;
import ownkey.application.notification.port.in.ManageNotificationUseCase;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.domain.notification.DeviceToken;
import ownkey.domain.notification.DeviceTokenRepository;
import ownkey.domain.notification.NotificationSetting;
import ownkey.domain.notification.NotificationSettingRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService implements ManageNotificationUseCase, ManageDeviceTokenUseCase {

    private final NotificationSettingRepository settingRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    // --- Notification Settings ---

    @Override
    @Transactional(readOnly = true)
    public NotificationResult.Settings getSettings(Long userId) {
        NotificationSetting setting = settingRepository.findById(userId)
                .orElse(new NotificationSetting(userId));

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

        boolean newCommunity = (command.community() != null) ? command.community() : setting.isCommunity();
        boolean newWiki = (command.wiki() != null) ? command.wiki() : setting.isWiki();
        boolean newShowmethekey = (command.showmethekey() != null) ? command.showmethekey() : setting.isShowmethekey();

        setting.update(newCommunity, newWiki, newShowmethekey);
    }

    // --- Device Tokens ---

    @Override
    public void register(Long userId, NotificationCommand.RegisterDevice command) {
        deviceTokenRepository.findByToken(command.deviceToken())
                .ifPresentOrElse(
                        existing -> {
                            if (!existing.getUserId().equals(userId)) {
                                deviceTokenRepository.delete(existing);
                                deviceTokenRepository.save(new DeviceToken(userId, command.deviceToken(), command.environment()));
                            }
                        },
                        () -> deviceTokenRepository.save(new DeviceToken(userId, command.deviceToken(), command.environment()))
                );
    }

    @Override
    public void unregister(Long userId, Long deviceId) {
        DeviceToken token = deviceTokenRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));

        if (!token.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        deviceTokenRepository.delete(token);
    }
}