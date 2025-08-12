package hyeok.board.articleread.service.response;

import hyeok.board.articleread.repository.ArticleQueryModel;

import java.time.LocalDateTime;

public record ArticleReadResponse(
        Long articleId,
        String title,
        String content,
        Long boardId,
        Long writerId,
        LocalDateTime createAt,
        LocalDateTime modifiedAt,
        Long articleCommentCount,
        Long articleLikeCount,
        Long articleViewCount
) {
    public static ArticleReadResponse from(ArticleQueryModel articleQueryModel, Long viewCount) {
        return new ArticleReadResponse(
                articleQueryModel.getArticleId(),
                articleQueryModel.getTitle(),
                articleQueryModel.getContent(),
                articleQueryModel.getBoardId(),
                articleQueryModel.getWriterId(),
                articleQueryModel.getCreateAt(),
                articleQueryModel.getModifiedAt(),
                articleQueryModel.getArticleCommentCount(),
                articleQueryModel.getArticleLikeCount(),
                viewCount
        );
    }
}
