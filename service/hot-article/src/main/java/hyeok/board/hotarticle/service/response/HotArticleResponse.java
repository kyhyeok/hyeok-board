package hyeok.board.hotarticle.service.response;

import hyeok.board.hotarticle.client.ArticleClient;

import java.time.LocalDateTime;

public record HotArticleResponse(
        Long articleId,
        String title,
        LocalDateTime createdAt
) {
    public static HotArticleResponse from(ArticleClient.ArticleResponse articleResponse) {
        return new HotArticleResponse(
                articleResponse.getArticleId(),
                articleResponse.getTitle(),
                articleResponse.getCreatedAt()
        );

    }
}
