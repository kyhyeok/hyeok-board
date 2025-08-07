package hyeok.board.hotarticle.service.eventhandler;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.ArticleViewedEventPayload;
import hyeok.board.hotarticle.repository.ArticleViewCountRepository;
import hyeok.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleViewEventHandler implements EventHandler<ArticleViewedEventPayload> {
    private final ArticleViewCountRepository aViewCountRepository;
    @Override
    public void handle(Event<ArticleViewedEventPayload> event) {
        ArticleViewedEventPayload payload = event.getPayload();
        aViewCountRepository.createOrUpdate(
                payload.articleId(),
                payload.articleViewCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<ArticleViewedEventPayload> event) {
        return EventType.ARTICLE_VIEWED == event.getType();
    }

    @Override
    public Long findArticleId(Event<ArticleViewedEventPayload> event) {
        return event.getPayload().articleId();
    }
}
