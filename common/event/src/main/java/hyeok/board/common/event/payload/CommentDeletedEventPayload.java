package hyeok.board.common.event.payload;

import hyeok.board.common.event.EventPayload;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDeletedEventPayload(
        Long commentId,
        String content,
        String path,
        Long articleId,
        Long writerId,
        Boolean deleted,
        LocalDateTime createdAt,
        Long articleCommentCount
) implements EventPayload {
}
