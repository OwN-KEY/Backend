package ownkey.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.user.application.result.ActivitySummaryResult;
import ownkey.user.application.result.CheckNicknameResult;
import ownkey.user.application.result.MyProfileResult;
import ownkey.user.application.usecase.CheckNicknameUseCase;
import ownkey.user.application.usecase.GetMyActivitiesUseCase;
import ownkey.user.application.usecase.GetMyProfileUseCase;
import ownkey.user.implement.UserReader;
import ownkey.user.domain.User;
import ownkey.common.response.CursorPage;
import ownkey.common.exception.BusinessException;
import ownkey.user.domain.vo.Nickname;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadService implements CheckNicknameUseCase, GetMyProfileUseCase, GetMyActivitiesUseCase {
    private final UserReader userReader;

    @Override
    public CheckNicknameResult isNicknameAvailable(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return new CheckNicknameResult(false);
        }
        try {
            Nickname validNickname = new Nickname(nickname);
            boolean exists = userReader.existsNickname(validNickname);
            return new CheckNicknameResult(!exists);
        } catch (BusinessException e) {
            return new CheckNicknameResult(false);
        }
    }

    @Override
    public MyProfileResult getProfile(Long userId) {
        User u = userReader.get(userId);
        return new MyProfileResult(
            u.getId(),
            u.getNickname().value(),
            u.getProfileImageId(),
            u.getBackgroundImageId(),
            u.getRole().name(),
            u.getStatus().name(),
            u.getCreatedAt()
        );
    }

    @Override
    public CursorPage<ActivitySummaryResult> getPosts(Long userId, Long cursorId, int limit) {
        // TODO: QueryDSL을 사용하여 cursorId보다 작은 ID를 가진 게시글을 limit + 1개 조회
        return CursorPage.empty();
    }

    @Override
    public CursorPage<ActivitySummaryResult> getBookmarks(Long userId, Long cursorId, int limit) {
        // TODO: Bookmark 엔티티 조인 조회 구현 필요
        return CursorPage.empty();
    }
}