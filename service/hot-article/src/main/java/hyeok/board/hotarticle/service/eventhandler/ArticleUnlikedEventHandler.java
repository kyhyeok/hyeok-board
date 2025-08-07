package hyeok.board.hotarticle.service.eventhandler;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.ArticleUnlikedEventPayload;
import hyeok.board.hotarticle.repository.ArticleLikeCountRepository;
import hyeok.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {
    private final ArticleLikeCountRepository articleLikeCountRepository;
    @Override
    public void handle(Event<ArticleUnlikedEventPayload> event) {
        ArticleUnlikedEventPayload payload = event.getPayload();
        articleLikeCountRepository.createOrUpdate(
                payload.articleId(),
                payload.articleLikeCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<ArticleUnlikedEventPayload> event) {
        return EventType.ARTICLE_UNLIKED == event.getType();
    }

    @Override
    public Long findArticleId(Event<ArticleUnlikedEventPayload> event) {
        return event.getPayload().articleId();
    }
}
