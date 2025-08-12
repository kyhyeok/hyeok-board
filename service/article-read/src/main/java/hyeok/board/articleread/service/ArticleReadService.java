package hyeok.board.articleread.service;

import hyeok.board.articleread.client.ArticleClient;
import hyeok.board.articleread.client.CommentClient;
import hyeok.board.articleread.client.LikeClient;
import hyeok.board.articleread.client.ViewClient;
import hyeok.board.articleread.repository.ArticleQueryModel;
import hyeok.board.articleread.repository.ArticleQueryModelRepository;
import hyeok.board.articleread.service.event.handler.EventHandler;
import hyeok.board.articleread.service.response.ArticleReadResponse;
import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.time.Duration.ofDays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleReadService {
    private final ArticleClient articleClient;

    private final CommentClient commentClient;

    private final LikeClient likeClient;

    private final ViewClient viewClient;

    private final ArticleQueryModelRepository articleQueryModelRepository;

    private final List<EventHandler> eventHandlers;

    public void handleEvent(Event<EventPayload> event) {
        for (EventHandler eventHandler : eventHandlers) {
            if (eventHandler.supports(event)) {
                eventHandler.handle(event);
            }
        }
    }

    public ArticleReadResponse read(Long articleId) {
        ArticleQueryModel articleQueryModel = articleQueryModelRepository
                .read(articleId)
                .or(() -> fetch(articleId))
                .orElseThrow();

        return ArticleReadResponse.from(
                articleQueryModel,
                viewClient.count(articleId)
        );
    }

    private Optional<ArticleQueryModel> fetch(Long articleId) {
        Optional<ArticleQueryModel> articleQueryModelOptional = articleClient.read(articleId)
                .map(article -> ArticleQueryModel.create(
                        article,
                        commentClient.count(articleId),
                        likeClient.count(articleId)
                ));
        articleQueryModelOptional
                .ifPresent(
                        articleQueryModel -> articleQueryModelRepository.create(articleQueryModel, ofDays(1))
                );

        log.info("[ArticleReadService.fetch] fetch data. articleId = {}, isPresent={}",
                articleId, articleQueryModelOptional.isPresent());
        return articleQueryModelOptional;
    }
}
