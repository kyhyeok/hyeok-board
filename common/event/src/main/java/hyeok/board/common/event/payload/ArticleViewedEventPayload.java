package hyeok.board.common.event.payload;

import hyeok.board.common.event.EventPayload;
import lombok.Builder;

@Builder
public record ArticleViewedEventPayload(
        Long articleId,
        Long articleViewCount
) implements EventPayload {
}
