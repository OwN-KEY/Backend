package ownkey.user.presentation.controller;

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
import ownkey.user.application.command.UpdateProfileCommand;
import ownkey.user.application.result.CheckNicknameResult;
import ownkey.user.application.result.MyProfileResult;
import ownkey.user.application.usecase.*;
import ownkey.user.presentation.request.UpdateProfileRequest;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(JacksonConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private UpdateProfileUseCase updateProfileUseCase;
    @MockitoBean
    private WithdrawUseCase withdrawUseCase;
    @MockitoBean
    private CheckNicknameUseCase checkNicknameUseCase;
    @MockitoBean
    private GetMyProfileUseCase getMyProfileUseCase;
    @MockitoBean
    private GetMyActivitiesUseCase getMyActivitiesUseCase;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final Long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        UserPrincipal principal = new UserPrincipal(TEST_USER_ID, "ROLE_USER");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 사용 가능한 경우")
    void checkNicknameAvailable() throws Exception {
        // given
        String nickname = "테스트닉네임";
        given(checkNicknameUseCase.isNicknameAvailable(nickname))
                .willReturn(new CheckNicknameResult(true));

        // when & then
        mockMvc.perform(get("/api/v1/me/check-nickname")
                        .param("nickname", nickname))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAvailable").value(true));
    }

    @Test
    @DisplayName("내 프로필 조회 성공")
    void getProfileSuccess() throws Exception {
        // given
        MyProfileResult result = new MyProfileResult(
                TEST_USER_ID, "닉네임", "img1", "bg1", "USER", "ACTIVE", LocalDateTime.now()
        );
        given(getMyProfileUseCase.getProfile(TEST_USER_ID)).willReturn(result);

        // when & then
        mockMvc.perform(get("/api/v1/me"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_USER_ID))
                .andExpect(jsonPath("$.nickname").value("닉네임"));
    }

    @Test
    @DisplayName("내 프로필 수정 성공")
    void updateProfileSuccess() throws Exception {
        // given
        UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", "newImg", null);

        // when & then
        mockMvc.perform(patch("/api/v1/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // verify
        verify(updateProfileUseCase).updateProfile(eq(TEST_USER_ID), any(UpdateProfileCommand.class));
    }
}