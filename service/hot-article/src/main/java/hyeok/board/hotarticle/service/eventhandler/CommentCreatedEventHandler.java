package hyeok.board.hotarticle.service.eventhandler;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.CommentCreatedEventPayload;
import hyeok.board.hotarticle.repository.ArticleCommentCountRepository;
import hyeok.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {
    private final ArticleCommentCountRepository articleCommentCountRepository;
    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        CommentCreatedEventPayload payload = event.getPayload();
        articleCommentCountRepository.createOrUpdate(
                payload.articleId(),
                payload.articleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED == event.getType();
    }

    @Override
    public Long findArticleId(Event<CommentCreatedEventPayload> event) {
        return event.getPayload().articleId();
    }
}
