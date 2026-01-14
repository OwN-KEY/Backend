package ownkey.user.application.result;

import java.time.LocalDateTime;

public record ActivitySummaryResult(
        Long id,
        String title,
        String contentPreview,
        LocalDateTime createdAt
) {
}
