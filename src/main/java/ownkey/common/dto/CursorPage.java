package ownkey.common.dto;

import java.util.List;

public record CursorPage<T>(
        List<T> content,
        String nextCursor,
        boolean hasNext
) {
    public static <T> CursorPage<T> of(List<T> content, String nextCursor, boolean hasNext) {
        return new CursorPage<>(content, nextCursor, hasNext);
    }
}