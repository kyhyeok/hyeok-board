package hyeok.board.common.event;

import hyeok.board.common.event.payload.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.HYEOK_BOARD_ARTICLE),
    ARTICLE_UPDATED(ArticleUnlikedEventPayload.class, Topic.HYEOK_BOARD_ARTICLE),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.HYEOK_BOARD_ARTICLE),
    COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.HYEOK_BOARD_COMMENT),
    COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.HYEOK_BOARD_COMMENT),
    ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.HYEOK_BOARD_LIKE),
    ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, Topic.HYEOK_BOARD_LIKE),
    ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.HYEOK_BOARD_VIEW)
    ;

    private final Class<? extends EventPayload> payloadClass;

    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String HYEOK_BOARD_ARTICLE = "hyeok-board-article";
        public static final String HYEOK_BOARD_COMMENT = "hyeok-board-comment";
        public static final String HYEOK_BOARD_LIKE = "hyeok-board-like";
        public static final String HYEOK_BOARD_VIEW = "hyeok-board-view";
    }
}
