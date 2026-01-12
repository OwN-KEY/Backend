package ownkey.presentation.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ownkey.application.auth.dto.AuthCommand;
import ownkey.application.auth.dto.AuthResult;
import ownkey.application.auth.usecase.KakaoLoginUseCase;
import ownkey.application.auth.usecase.LogoutUseCase;
import ownkey.application.auth.usecase.TokenRefreshUseCase;

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
    public ResponseEntity<AuthResult.TokenResponse> kakaoLogin(@RequestBody AuthCommand.KakaoLogin command) {
        // 👇 [감시 카메라] 들어온 토큰 값을 콘솔에 출력합니다.
        System.out.println("==========================================");
        System.out.println("컨트롤러 도착! 받은 ID Token: " + command.idToken());
        System.out.println("==========================================");

        return ResponseEntity.ok(kakaoLoginUseCase.login(command));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
    public ResponseEntity<AuthResult.TokenResponse> refresh(@RequestBody AuthCommand.Refresh command) {
        return ResponseEntity.ok(tokenRefreshUseCase.refresh(command));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Refresh Token을 삭제하고 Access Token을 블랙리스트에 등록합니다.")
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @RequestBody(required = false) AuthCommand.Refresh refreshCommand
    ) {
        String accessToken = bearerToken.substring(7);
        String refreshToken = (refreshCommand != null) ? refreshCommand.refreshToken() : null;

        logoutUseCase.logout(new AuthCommand.Logout(accessToken, refreshToken));
        return ResponseEntity.noContent().build();
    }
}