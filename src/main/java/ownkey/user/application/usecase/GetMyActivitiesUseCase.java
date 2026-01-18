package ownkey.user.application.usecase;

import ownkey.common.response.CursorPage;
import ownkey.user.application.result.ActivitySummaryResult;

public interface GetMyActivitiesUseCase {
    CursorPage<ActivitySummaryResult> getPosts(Long userId, Long cursorId, int limit);
    CursorPage<ActivitySummaryResult> getBookmarks(Long userId, Long cursorId, int limit);
}