package hyeok.board.articleread.service.event.handler;

import hyeok.board.articleread.repository.ArticleIdListRepository;
import hyeok.board.articleread.repository.ArticleQueryModelRepository;
import hyeok.board.articleread.repository.BoardArticleCountRepository;
import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();

        articleIdListRepository.delete(payload.boardId(), payload.articleId());

        articleQueryModelRepository.delete(payload.articleId());

        boardArticleCountRepository.createOrUpdate(payload.boardId(), payload.boardArticleCount());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }
}
