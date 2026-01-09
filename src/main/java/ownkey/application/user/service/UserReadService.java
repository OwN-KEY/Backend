package ownkey.application.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.user.dto.UserResult;
import ownkey.application.user.port.in.CheckNicknameUseCase;
import ownkey.application.user.port.in.GetMyActivitiesUseCase;
import ownkey.application.user.port.in.GetMyProfileUseCase;
import ownkey.common.dto.CursorPage;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.domain.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadService implements CheckNicknameUseCase, GetMyProfileUseCase, GetMyActivitiesUseCase {
    private final UserRepository userRepository;

    @Override
    public boolean execute(String nickname) {
        if (nickname == null || nickname.isBlank()) return false;
        // 중복 : true, 사용 가능 : false
        return userRepository.existsByNickname(nickname.strip());
    }

    @Override
    public UserResult.MyProfile execute(Long userId) {
        return userRepository.findById(userId)
                .map(u -> new UserResult.MyProfile(
                        u.getId(),
                        u.getNickname(),
                        u.getProfileImageId(),
                        u.getBackgroundImageId(),
                        u.getRole().name(),
                        u.getCreatedAt()
                ))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public CursorPage<UserResult.ActivitySummary> getPosts(Long userId, Long cursorId, int limit) {
        // TODO: QueryDSL을 사용하여 cursorId보다 작은 ID를 가진 게시글을 limit + 1개 조회

        // 임시 리스트
        List<UserResult.ActivitySummary> content = Collections.emptyList();
        String nextCursor = null;
        boolean hasNext = false;

        return CursorPage.of(content, nextCursor, hasNext);
    }

    @Override
    public CursorPage<UserResult.ActivitySummary> getBookmarks(Long userId, Long cursorId, int limit) {
        // TODO: Bookmark 엔티티 조인 조회 구현 필요
        return CursorPage.of(Collections.emptyList(), null, false);
    }
}