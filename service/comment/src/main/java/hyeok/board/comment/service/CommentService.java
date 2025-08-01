package hyeok.board.comment.service;

import hyeok.board.comment.entity.Comment;
import hyeok.board.comment.repository.CommentRepository;
import hyeok.board.comment.service.request.CommentCreateRequest;
import hyeok.board.comment.service.response.CommentResponse;
import hyeok.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final Snowflake snowflake = new Snowflake();

    @Transactional
    public CommentResponse create(CommentCreateRequest commentRequest) {
        Comment parent = findParent(commentRequest);

        Comment comment = commentRepository.save(
                Comment.create(
                        snowflake.nextId(),
                        commentRequest.content(),
                        parent == null ? null : parent.getParentCommentId(),
                        commentRequest.articleId(),
                        commentRequest.writerId()
                )
        );

        return CommentResponse.from(comment);
    }

    private Comment findParent(CommentCreateRequest commentRequest) {
        Long parentCommentId = commentRequest.parentCommentId();

        if (parentCommentId == null) {
            return null;
        }

        return commentRepository.findById(parentCommentId)
                .filter(not(Comment::getDeleted))
                .filter(Comment::isRoot)
                .orElseThrow();
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(
                commentRepository.findById(commentId).orElseThrow()
        );
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }
                });
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);
        if (!comment.isRoot()) {
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }
}
