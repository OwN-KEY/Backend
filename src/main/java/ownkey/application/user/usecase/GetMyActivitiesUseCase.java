package ownkey.application.user.usecase;

import ownkey.application.user.dto.UserResult;
import ownkey.presentation.common.response.CursorPage;

public interface GetMyActivitiesUseCase {
    CursorPage<UserResult.ActivitySummary> getPosts(Long userId, Long cursorId, int limit);
    CursorPage<UserResult.ActivitySummary> getBookmarks(Long userId, Long cursorId, int limit);
}