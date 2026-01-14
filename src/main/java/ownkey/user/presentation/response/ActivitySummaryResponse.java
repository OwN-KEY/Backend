package ownkey.user.presentation.response;
import java.time.LocalDateTime;

public record ActivitySummaryResponse(
        Long id,
        String title,
        String contentPreview,
        LocalDateTime createdAt
) {}