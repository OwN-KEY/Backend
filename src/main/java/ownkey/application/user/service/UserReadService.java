package ownkey.application.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.user.dto.UserResult;
import ownkey.application.user.usecase.CheckNicknameUseCase;
import ownkey.application.user.usecase.GetMyActivitiesUseCase;
import ownkey.application.user.usecase.GetMyProfileUseCase;
import ownkey.implement.user.component.UserReader;
import ownkey.implement.user.model.User;
import ownkey.presentation.common.response.CursorPage;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.implement.user.model.vo.Nickname;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadService implements CheckNicknameUseCase, GetMyProfileUseCase, GetMyActivitiesUseCase {
    private final UserReader userReader;

    @Override
    public boolean isNicknameAvailable(String nickname) {
        if (nickname == null || nickname.isBlank()) return false;
        try {
            Nickname validNickname = new Nickname(nickname);
            return !userReader.existsNickname(validNickname);
        } catch (BusinessException e) {
            return false;
        }
    }

    @Override
    public UserResult.MyProfile getProfile(Long userId) {
        User u = userReader.get(userId);
        return new UserResult.MyProfile(
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
    public CursorPage<UserResult.ActivitySummary> getPosts(Long userId, Long cursorId, int limit) {
        // TODO: QueryDSL을 사용하여 cursorId보다 작은 ID를 가진 게시글을 limit + 1개 조회
        return CursorPage.empty();
    }

    @Override
    public CursorPage<UserResult.ActivitySummary> getBookmarks(Long userId, Long cursorId, int limit) {
        // TODO: Bookmark 엔티티 조인 조회 구현 필요
        return CursorPage.empty();
    }
}