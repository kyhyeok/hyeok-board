package hyeok.board.common.event.payload;

import hyeok.board.common.event.EventPayload;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleUpdatedEventPayload(
        Long articleId,
        String title,
        String content,
        Long boardId,
        Long writerId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) implements EventPayload {
}
