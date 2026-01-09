package ownkey.application.user.port.in;

import ownkey.application.user.dto.UserResult;
import ownkey.common.dto.CursorPage;

public interface GetMyActivitiesUseCase {
    CursorPage<UserResult.ActivitySummary> getPosts(Long userId, Long cursorId, int limit);
    CursorPage<UserResult.ActivitySummary> getBookmarks(Long userId, Long cursorId, int limit);
}