package hyeok.board.common.event.payload;

import hyeok.board.common.event.EventPayload;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleDeletedEventPayload(
        Long articleId,
        String title,
        String content,
        Long boardId,
        Long writerId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long boardArticleCount
) implements EventPayload {
}
