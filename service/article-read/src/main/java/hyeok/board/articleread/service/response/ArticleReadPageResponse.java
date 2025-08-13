package hyeok.board.articleread.service.response;

import java.util.List;

public record ArticleReadPageResponse(
        List<ArticleReadResponse> articles,
        Long articleCount
) {
    public static ArticleReadPageResponse of(List<ArticleReadResponse> articles, Long articleCount) {
        return new ArticleReadPageResponse(articles, articleCount);
    }
}
