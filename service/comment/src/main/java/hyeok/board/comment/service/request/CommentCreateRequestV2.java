package hyeok.board.comment.service.request;

public record CommentCreateRequestV2(
        Long articleId,
        String content,
        String parentPath,
        Long writerId
) {
}
