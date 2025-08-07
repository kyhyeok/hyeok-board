package hyeok.board.hotarticle.service.eventhandler;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.CommentDeletedEventPayload;
import hyeok.board.hotarticle.repository.ArticleCommentCountRepository;
import hyeok.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {
    private final ArticleCommentCountRepository aCommentCountRepository;
    @Override
    public void handle(Event<CommentDeletedEventPayload> event) {
        CommentDeletedEventPayload payload = event.getPayload();
        aCommentCountRepository.createOrUpdate(
                payload.articleId(),
                payload.articleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<CommentDeletedEventPayload> event) {
        return EventType.COMMENT_DELETED == event.getType();
    }

    @Override
    public Long findArticleId(Event<CommentDeletedEventPayload> event) {
        return event.getPayload().articleId();
    }
}
