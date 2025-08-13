package hyeok.board.articleread.service.event.handler;

import hyeok.board.articleread.repository.ArticleIdListRepository;
import hyeok.board.articleread.repository.ArticleQueryModel;
import hyeok.board.articleread.repository.ArticleQueryModelRepository;
import hyeok.board.articleread.repository.BoardArticleCountRepository;
import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.ArticleCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.Duration.ofDays;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();

        articleQueryModelRepository.create(
                ArticleQueryModel.create(payload),
                ofDays(1)
        );

        articleIdListRepository.add(payload.boardId(), payload.articleId(), 1_000L);

        boardArticleCountRepository.createOrUpdate(payload.boardId(), payload.boardArticleCount());
    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType();
    }
}
