package hyeok.board.articleread.service;

import hyeok.board.articleread.client.ArticleClient;
import hyeok.board.articleread.client.CommentClient;
import hyeok.board.articleread.client.LikeClient;
import hyeok.board.articleread.client.ViewClient;
import hyeok.board.articleread.repository.ArticleIdListRepository;
import hyeok.board.articleread.repository.ArticleQueryModel;
import hyeok.board.articleread.repository.ArticleQueryModelRepository;
import hyeok.board.articleread.repository.BoardArticleCountRepository;
import hyeok.board.articleread.service.event.handler.EventHandler;
import hyeok.board.articleread.service.response.ArticleReadPageResponse;
import hyeok.board.articleread.service.response.ArticleReadResponse;
import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final ArticleIdListRepository articleIdListRepository;

    private BoardArticleCountRepository boardArticleCountRepository;

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

    public ArticleReadPageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticleReadPageResponse.of(
                readAll(readAllArticleIds(boardId, page, pageSize)),
                count(boardId)
        );
    }

    private List<ArticleReadResponse> readAll(List<Long> articleIds) {
        Map<Long, ArticleQueryModel> articleQueryModelMap = articleQueryModelRepository.readAll(articleIds);
        return articleIds.stream()
                .map(articleId -> articleQueryModelMap.containsKey(articleId)
                        ? articleQueryModelMap.get(articleId)
                        : fetch(articleId).orElse(null))
                .filter(Objects::nonNull)
                .map(articleQueryModel -> ArticleReadResponse.from(
                        articleQueryModel,
                        viewClient.count(articleQueryModel.getArticleId())
                ))
                .toList();
    }

    private List<Long> readAllArticleIds(Long boardId, Long page, Long pageSize) {
        List<Long> articleIds = articleIdListRepository.readAll(boardId, (page - 1) * pageSize, pageSize);

        if (pageSize == articleIds.size()) {
            log.info("[ArticleReadService.articleIdListRepository] return redis data.");
            return articleIds;
        }

        log.info("[ArticleReadService.articleIdListRepository] return origin data.");
        return articleClient.readAll(boardId, page, pageSize).getArticles().stream()
                .map(ArticleClient.ArticleResponse::getArticleId)
                .toList();
    }

    private long count(Long boardId) {
        Long result = boardArticleCountRepository.read(boardId);

        if (result != null) {
            return result;
        }
        long count = articleClient.count(boardId);

        boardArticleCountRepository.createOrUpdate(boardId, count);

        return count;
    }

    public List<ArticleReadResponse> readAllInfiniteScroll(Long boardId, Long lastArticleId, Long pageSize) {
        return readAll(readAllInfiniteScrollArticleIds(boardId, lastArticleId, pageSize));
    }

    private List<Long> readAllInfiniteScrollArticleIds(Long boardId, Long lastArticleId, Long pageSize) {
        List<Long> articleIds = articleIdListRepository.readAllInfiniteScroll(boardId, lastArticleId, pageSize);

        if (pageSize == articleIds.size()) {
            log.info("[ArticleReadService.readAllInfiniteScrollArticleIds] return redis data.");
            return articleIds;
        }

        log.info("[ArticleReadService.readAllInfiniteScrollArticleIds] origin redis data.");

        return articleClient.readAllInfiniteScroll(boardId, lastArticleId, pageSize).stream()
                .map(ArticleClient.ArticleResponse::getArticleId)
                .toList();

    }
}
