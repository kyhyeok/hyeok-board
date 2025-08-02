package hyeok.board.comment.service.response;

import hyeok.board.comment.entity.Comment;
import hyeok.board.comment.entity.CommentV2;

import java.time.LocalDateTime;

public record CommentResponseV2(
        Long commentId,
        String content,
        String path,
        Long articleId, // shard key
        Long writerId,
        Boolean deleted,
        LocalDateTime createdAt
) {
    public static CommentResponseV2 from(CommentV2 comment) {
        return new CommentResponseV2(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCommentPath().getPath(),
                comment.getArticleId(),
                comment.getWriterId(),
                comment.getDeleted(),
                comment.getCreatedAt()
        );
    }
}
