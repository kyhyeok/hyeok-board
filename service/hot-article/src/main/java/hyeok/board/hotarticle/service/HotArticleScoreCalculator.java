package hyeok.board.hotarticle.service;

import hyeok.board.hotarticle.repository.ArticleCommentCountRepository;
import hyeok.board.hotarticle.repository.ArticleLikeCountRepository;
import hyeok.board.hotarticle.repository.ArticleViewCountRepository;
import org.springframework.stereotype.Component;

@Component
public record HotArticleScoreCalculator(
        ArticleLikeCountRepository articleLikeCountRepository,

        ArticleCommentCountRepository articleCommentCountRepository,

        ArticleViewCountRepository articleViewCountRepository
        ) {
    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;

    public long calculate(Long articleId) {
        Long articleLikeCount = articleLikeCountRepository.read(articleId);
        Long articleCommentCount = articleCommentCountRepository.read(articleId);
        Long articleViewCount = articleViewCountRepository.read(articleId);
        return (articleLikeCount * ARTICLE_LIKE_COUNT_WEIGHT)
                + (articleCommentCount * ARTICLE_COMMENT_COUNT_WEIGHT)
                + articleViewCount;
    }
}
