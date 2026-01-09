package ownkey.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ownkey.application.user.dto.UserCommand;
import ownkey.application.user.dto.UserResult;
import ownkey.application.user.port.in.*;
import ownkey.common.dto.CursorPage;
import ownkey.common.security.UserPrincipal;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/me")
@Tag(name = "사용자", description = "마이페이지(정보 수정, 탈퇴)")
@RequiredArgsConstructor
public class UserController {

    private final UpdateProfileUseCase updateProfileUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final CheckNicknameUseCase checkNicknameUseCase;
    private final GetMyProfileUseCase getMyProfileUseCase;
    private final GetMyActivitiesUseCase getMyActivitiesUseCase;

    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복 확인", description = "입력한 닉네임의 사용 가능 여부를 확인합니다.")
    public ResponseEntity<Map<String, Boolean>> checkNickname(
            @Parameter(description = "확인할 닉네임", required = true)
            @RequestParam("nickname") String nickname
    ) {
        boolean isDuplicate = checkNicknameUseCase.execute(nickname);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    @PatchMapping
    @Operation(summary = "내 프로필 수정", description = "닉네임(2~8자) 및 프로필/배경 이미지를 수정합니다.")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserCommand.UpdateProfile command
    ) {
        updateProfileUseCase.execute(userPrincipal.userId(), command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "내 프로필 조회", description = "로그인한 사용자의 상세 프로필(가입일 포함)을 조회합니다.")
    public ResponseEntity<UserResult.MyProfile> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(getMyProfileUseCase.execute(userPrincipal.userId()));
    }

    @GetMapping("/posts")
    @Operation(summary = "내가 쓴 글 조회", description = "사용자가 작성한 게시글 목록을 조회합니다. (커서 페이징)")
    public ResponseEntity<CursorPage<UserResult.ActivitySummary>> getMyPosts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "마지막으로 조회한 게시글 ID (첫 요청 시 생략)") @RequestParam(required = false) Long cursor,
            @Parameter(description = "한 번에 가져올 개수") @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(getMyActivitiesUseCase.getPosts(userPrincipal.userId(), cursor, limit));
    }

    @GetMapping("/bookmarks")
    @Operation(summary = "북마크 조회", description = "사용자가 북마크한 게시글 목록을 조회합니다. (커서 페이징)")
    public ResponseEntity<CursorPage<UserResult.ActivitySummary>> getMyBookmarks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "마지막으로 조회한 북마크 ID (첫 요청 시 생략)") @RequestParam(required = false) Long cursor,
            @Parameter(description = "한 번에 가져올 개수") @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(getMyActivitiesUseCase.getBookmarks(userPrincipal.userId(), cursor, limit));
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "카카오 연결 해제(Admin Key 이용) 성공 시에만 탈퇴 처리됩니다.")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String ignoredToken
    ) {
        withdrawUseCase.execute(userPrincipal.userId());
        return ResponseEntity.noContent().build();
    }

}