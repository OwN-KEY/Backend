package ownkey.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ownkey.auth.application.command.KakaoLoginCommand;
import ownkey.auth.application.command.LogoutCommand;
import ownkey.auth.application.command.TokenRefreshCommand;
import ownkey.auth.application.result.AuthTokenResult;
import ownkey.auth.application.usecase.KakaoLoginUseCase;
import ownkey.auth.application.usecase.LogoutUseCase;
import ownkey.auth.application.usecase.TokenRefreshUseCase;
import ownkey.auth.presentation.request.KakaoLoginRequest;
import ownkey.auth.presentation.request.LogoutRequest;
import ownkey.auth.presentation.request.TokenRefreshRequest;
import ownkey.auth.presentation.response.AuthTokenResponse;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "인증", description = "로그인/로그아웃/토큰 관리")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final TokenRefreshUseCase tokenRefreshUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인 (OIDC)", description = "iOS 앱에서 받은 idToken을 검증하여 로그인합니다.")
    public ResponseEntity<AuthTokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        KakaoLoginCommand command = new KakaoLoginCommand(request.idToken());
        AuthTokenResult result = kakaoLoginUseCase.login(command);

        return ResponseEntity.ok(new AuthTokenResponse(result.accessToken(), result.refreshToken()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
    public ResponseEntity<AuthTokenResponse> refresh(@RequestBody TokenRefreshRequest request) {
        TokenRefreshCommand command = new TokenRefreshCommand(request.refreshToken());
        AuthTokenResult result = tokenRefreshUseCase.refresh(command);

        return ResponseEntity.ok(new AuthTokenResponse(result.accessToken(), result.refreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Refresh Token을 삭제하고 Access Token을 블랙리스트에 등록합니다.")
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @RequestBody(required = false) LogoutRequest request
    ) {
        String accessToken = bearerToken.substring(7);
        String refreshToken = (request != null) ? request.refreshToken() : null;

        logoutUseCase.logout(new LogoutCommand(accessToken, refreshToken));
        return ResponseEntity.noContent().build();
    }
}