package hyeok.board.view.service;

import hyeok.board.common.event.EventType;
import hyeok.board.common.event.payload.ArticleViewedEventPayload;
import hyeok.board.common.outboxmessagerelay.OutboxEventPublisher;
import hyeok.board.view.entity.ArticleViewCount;
import hyeok.board.view.repository.ArticleViewCountBackUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackUpProcessor {
    private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

    private final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    public void backUp(Long articleId, Long viewCount) {
        int result = articleViewCountBackUpRepository.updateViewCount(articleId, viewCount);
        if (result == 0) {
            articleViewCountBackUpRepository.findById(articleId)
                .ifPresentOrElse(
                    ignored -> {},
                    () -> articleViewCountBackUpRepository.save(ArticleViewCount.init(articleId, viewCount))
                );
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_VIEWED,
                ArticleViewedEventPayload.builder()
                        .articleId(articleId)
                        .articleViewCount(viewCount)
                        .build(),
                articleId
        );
    }
}
