package ownkey.presentation.user.controller;

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
import ownkey.application.user.dto.UserCommand;
import ownkey.application.user.dto.UserResult;
import ownkey.application.user.usecase.*;
import ownkey.infrastructure.common.config.JacksonConfig;
import ownkey.presentation.common.security.filter.JwtAuthenticationFilter;
import ownkey.presentation.common.security.principal.UserPrincipal;
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
        String nickname = "테스트닉네임";
        given(checkNicknameUseCase.isNicknameAvailable(nickname)).willReturn(true);

        mockMvc.perform(get("/api/v1/me/check-nickname")
                        .param("nickname", nickname))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAvailable").value(true));
    }

    @Test
    @DisplayName("내 프로필 조회 성공")
    void getProfileSuccess() throws Exception {
        UserResult.MyProfile profile = new UserResult.MyProfile(
                TEST_USER_ID, "닉네임", "img1", "bg1", "USER", "ACTIVE", LocalDateTime.now()
        );
        given(getMyProfileUseCase.getProfile(TEST_USER_ID)).willReturn(profile);

        mockMvc.perform(get("/api/v1/me"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_USER_ID));
    }

    @Test
    @DisplayName("내 프로필 수정 성공")
    void updateProfileSuccess() throws Exception {
        UserCommand.UpdateProfile command = new UserCommand.UpdateProfile("새닉네임", "newImg", null);

        mockMvc.perform(patch("/api/v1/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(updateProfileUseCase).updateProfile(eq(TEST_USER_ID), any(UserCommand.UpdateProfile.class));
    }
}