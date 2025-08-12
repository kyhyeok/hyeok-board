package hyeok.board.articleread.repository;

import hyeok.board.articleread.client.ArticleClient;
import hyeok.board.common.event.payload.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleQueryModel {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Long articleCommentCount;
    private Long articleLikeCount;

    public static ArticleQueryModel create(ArticleCreatedEventPayload payload) {
        ArticleQueryModel articleQueryModel = new ArticleQueryModel();
        articleQueryModel.articleId = payload.articleId();
        articleQueryModel.title = payload.title();
        articleQueryModel.content = payload.content();
        articleQueryModel.boardId = payload.boardId();
        articleQueryModel.writerId = payload.writerId();
        articleQueryModel.createAt = payload.createdAt();
        articleQueryModel.modifiedAt = payload.modifiedAt();
        articleQueryModel.articleCommentCount = 0L;
        articleQueryModel.articleLikeCount = 0L;
        return articleQueryModel;
    }

    public static ArticleQueryModel create(ArticleClient.ArticleResponse article, Long commentCount, Long likeCount) {
        ArticleQueryModel articleQueryModel = new ArticleQueryModel();
        articleQueryModel.articleId = article.getArticleId();
        articleQueryModel.title = article.getTitle();
        articleQueryModel.content = article.getContent();
        articleQueryModel.boardId = article.getBoardId();
        articleQueryModel.writerId = article.getWriterId();
        articleQueryModel.createAt = article.getCreatedAt();
        articleQueryModel.modifiedAt = article.getModifiedAt();
        articleQueryModel.articleCommentCount = commentCount;
        articleQueryModel.articleLikeCount = likeCount;
        return articleQueryModel;
    }

    public void updateBy(CommentCreatedEventPayload payload) {
        this.articleCommentCount = payload.articleCommentCount();
    }

    public void updateBy(CommentDeletedEventPayload payload) {
        this.articleCommentCount = payload.articleCommentCount();
    }

    public void updateBy(ArticleLikedEventPayload payload) {
        this.articleLikeCount = payload.articleLikeCount();
    }

    public void updateBy(ArticleUnlikedEventPayload payload) {
        this.articleLikeCount = payload.articleLikeCount();
    }

    public void updateBy(ArticleUpdatedEventPayload payload) {
        this.title = payload.title();
        this.content = payload.content();
        this.boardId = payload.boardId();
        this.writerId = payload.writerId();
        this.createAt = payload.createdAt();
        this.modifiedAt = payload.modifiedAt();
    }
}
