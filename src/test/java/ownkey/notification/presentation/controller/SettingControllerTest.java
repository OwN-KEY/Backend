package ownkey.notification.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ownkey.common.config.JacksonConfig;
import ownkey.common.security.filter.JwtAuthenticationFilter;
import ownkey.common.security.principal.UserPrincipal;
import ownkey.notification.application.command.RegisterDeviceCommand;
import ownkey.notification.application.command.UpdateSettingsCommand;
import ownkey.notification.application.result.SettingsResult;
import ownkey.notification.application.usecase.ManageDeviceTokenUseCase;
import ownkey.notification.application.usecase.ManageNotificationUseCase;
import ownkey.notification.presentation.request.RegisterDeviceRequest;
import ownkey.notification.presentation.request.UpdateSettingsRequest;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SettingsController.class)
@AutoConfigureMockMvc(addFilters = false) // Security Filter 비활성화
@Import(JacksonConfig.class)
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private ManageNotificationUseCase notificationUseCase;

    @MockitoBean
    private ManageDeviceTokenUseCase deviceTokenUseCase;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final Long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        // @AuthenticationPrincipal이 동작하도록 가짜 인증 정보 주입
        UserPrincipal principal = new UserPrincipal(TEST_USER_ID, "ROLE_USER");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("알림 설정 조회 성공")
    void getNotificationSettings() throws Exception {
        // given
        SettingsResult result = new SettingsResult(true, false, true);
        given(notificationUseCase.getSettings(TEST_USER_ID)).willReturn(result);

        // when & then
        mockMvc.perform(get("/api/v1/me/notification-settings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.community").value(true))
                .andExpect(jsonPath("$.wiki").value(false))
                .andExpect(jsonPath("$.showmethekey").value(true));
    }

    @Test
    @DisplayName("알림 설정 수정 성공")
    void updateNotificationSettings() throws Exception {
        // given
        UpdateSettingsRequest request = new UpdateSettingsRequest(false, true, false);

        // when & then
        mockMvc.perform(put("/api/v1/me/notification-settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // verify: 서비스가 올바른 Command로 호출되었는지 검증
        verify(notificationUseCase).updateSettings(eq(TEST_USER_ID), any(UpdateSettingsCommand.class));
    }

    @Test
    @DisplayName("디바이스 토큰 등록 성공")
    void registerDevice() throws Exception {
        // given
        RegisterDeviceRequest request = new RegisterDeviceRequest("test-device-token", "sandbox");

        // when & then
        mockMvc.perform(post("/api/v1/me/devices/apns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deviceTokenUseCase).registerDevice(eq(TEST_USER_ID), any(RegisterDeviceCommand.class));
    }

    @Test
    @DisplayName("디바이스 토큰 삭제 성공")
    void unregisterDevice() throws Exception {
        // given
        Long deviceId = 100L;

        // when & then
        mockMvc.perform(delete("/api/v1/me/devices/apns/{deviceId}", deviceId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deviceTokenUseCase).unregisterDevice(TEST_USER_ID, deviceId);
    }
}