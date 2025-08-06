package hyeok.board.common.event.payload;

import hyeok.board.common.event.EventPayload;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleUnlikedEventPayload(
        Long articleLikeId,
        Long articleId,
        Long userId,
        LocalDateTime createdAt,
        Long articleLikeCount
) implements EventPayload {
}
