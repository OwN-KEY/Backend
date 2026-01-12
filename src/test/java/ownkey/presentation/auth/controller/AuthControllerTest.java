package ownkey.presentation.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;
import ownkey.application.auth.usecase.KakaoLoginUseCase;
import ownkey.application.auth.usecase.LogoutUseCase;
import ownkey.application.auth.usecase.TokenRefreshUseCase;
import ownkey.infrastructure.common.config.JacksonConfig;
import ownkey.presentation.common.security.filter.JwtAuthenticationFilter;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(JacksonConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private KakaoLoginUseCase kakaoLoginUseCase;

    @MockitoBean
    private TokenRefreshUseCase tokenRefreshUseCase;

    @MockitoBean
    private LogoutUseCase logoutUseCase;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("카카오 로그인 성공 시 액세스 토큰과 리프레시 토큰을 반환한다")
    void kakaoLoginSuccess() throws Exception {
        // given
        AuthCommand.KakaoLogin command = new AuthCommand.KakaoLogin("test-id-token");
        AuthResult.TokenResponse response = new AuthResult.TokenResponse("access-token", "refresh-token");

        given(kakaoLoginUseCase.login(any(AuthCommand.KakaoLogin.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/auth/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command))) // jsonMapper 사용
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급에 성공한다")
    void refreshSuccess() throws Exception {
        // given
        AuthCommand.Refresh command = new AuthCommand.Refresh("valid-refresh-token");
        AuthResult.TokenResponse response = new AuthResult.TokenResponse("new-access-token", "new-refresh-token");

        given(tokenRefreshUseCase.refresh(any(AuthCommand.Refresh.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"));
    }

    @Test
    @DisplayName("로그아웃 요청 시 성공적으로 처리된다")
    void logoutSuccess() throws Exception {
        // given
        String accessToken = "Bearer access-token";
        AuthCommand.Refresh command = new AuthCommand.Refresh("refresh-token");

        // when & then
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(logoutUseCase).logout(any(AuthCommand.Logout.class));
    }
}