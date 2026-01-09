package ownkey.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ownkey.application.notification.dto.NotificationCommand;
import ownkey.application.notification.dto.NotificationResult;
import ownkey.application.notification.port.in.ManageDeviceTokenUseCase;
import ownkey.application.notification.port.in.ManageNotificationUseCase;
import ownkey.common.security.UserPrincipal;

@RestController
@RequestMapping("/api/v1/me")
@Tag(name = "설정", description = "알림 설정 및 디바이스 토큰 관리")
@RequiredArgsConstructor
public class SettingsController {

    private final ManageNotificationUseCase notificationUseCase;
    private final ManageDeviceTokenUseCase deviceTokenUseCase;

    @GetMapping("/notification-settings")
    @Operation(summary = "알림 설정 조회", description = "사용자의 알림 수신 여부 설정을 조회합니다.")
    public ResponseEntity<NotificationResult.Settings> getNotificationSettings(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(notificationUseCase.getSettings(userPrincipal.userId()));
    }

    @PutMapping("/notification-settings")
    @Operation(summary = "알림 설정 수정", description = "알림 수신 여부를 변경합니다.")
    public ResponseEntity<Void> updateNotificationSettings(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody NotificationCommand.UpdateSettings command
    ) {
        notificationUseCase.updateSettings(userPrincipal.userId(), command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/devices/apns")
    @Operation(summary = "APNs 토큰 등록", description = "iOS 디바이스 토큰을 등록합니다.")
    public ResponseEntity<Void> registerDevice(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody NotificationCommand.RegisterDevice command
    ) {
        deviceTokenUseCase.register(userPrincipal.userId(), command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/devices/apns/{deviceId}")
    @Operation(summary = "디바이스 토큰 삭제", description = "등록된 디바이스 토큰을 해제합니다.")
    public ResponseEntity<Void> unregisterDevice(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long deviceId
    ) {
        deviceTokenUseCase.unregister(userPrincipal.userId(), deviceId);
        return ResponseEntity.noContent().build();
    }
}