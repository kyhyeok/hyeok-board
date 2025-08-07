package hyeok.board.hotarticle.service.eventhandler;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.ArticleDeletedEventPayload;
import hyeok.board.hotarticle.repository.ArticleCreatedTimeRepository;
import hyeok.board.hotarticle.repository.HotArticleListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final HotArticleListRepository hotArticleListRepository;

    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        articleCreatedTimeRepository.delete(payload.articleId());
        hotArticleListRepository.remove(payload.articleId(), payload.createdAt());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }

    @Override
    public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
        return event.getPayload().articleId();
    }
}
