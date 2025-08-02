package hyeok.board.comment.service.response;

import java.util.List;

public record CommentPageResponseV2(
        List<CommentResponseV2> comments,
        Long commentCount
) {
    public static CommentPageResponseV2 of(List<CommentResponseV2> comments, Long commentCount) {
        return new CommentPageResponseV2(comments, commentCount);
    }
}
