package hyeok.board.comment.service;

import hyeok.board.comment.entity.ArticleCommentCount;
import hyeok.board.comment.entity.CommentPath;
import hyeok.board.comment.entity.CommentV2;
import hyeok.board.comment.repository.ArticleCommentCountRepository;
import hyeok.board.comment.repository.CommentRepositoryV2;
import hyeok.board.comment.service.request.CommentCreateRequestV2;
import hyeok.board.comment.service.response.CommentPageResponseV2;
import hyeok.board.comment.service.response.CommentResponseV2;
import hyeok.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
    private final Snowflake snowflake = new Snowflake();

    private final CommentRepositoryV2 commentRepository;

    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Transactional
    public CommentResponseV2 create(CommentCreateRequestV2 createRequest) {
        CommentV2 parent = findParent(createRequest);

        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();

        String descendantsTopPath = commentRepository
                .findDescendantsTopPath(createRequest.articleId(), parentCommentPath.getPath())
                .orElse(null);

        CommentV2 commentV2 = CommentV2.create(
                snowflake.nextId(),
                createRequest.content(),
                createRequest.articleId(),
                createRequest.writerId(),
                parentCommentPath.createChildCommentPath(descendantsTopPath)
        );

        int result = articleCommentCountRepository.increase(createRequest.articleId());

        if (result == 0) {
            articleCommentCountRepository.save(
                    ArticleCommentCount.init(createRequest.articleId(), 1L)
            );
        }

        CommentV2 comment = commentRepository.save(commentV2);

        return CommentResponseV2.from(comment);
    }

    private CommentV2 findParent(CommentCreateRequestV2 createRequest) {
        String parentPath = createRequest.parentPath();

        if (parentPath == null) {
            return null;
        }

        return commentRepository.findByPath(parentPath)
                .filter(not(CommentV2::getDeleted))
                .orElseThrow();
    }

    public CommentResponseV2 read(Long commentId) {
        return CommentResponseV2.from(commentRepository.findById(commentId).orElseThrow());
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository
                .findById(commentId)
                .filter(not(CommentV2::getDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }
                });
    }

    private boolean hasChildren(CommentV2 comment) {
        return commentRepository.findDescendantsTopPath(
                comment.getArticleId(),
                comment.getCommentPath().getPath()
        ).isPresent();
    }

    private void delete(CommentV2 comment) {
        commentRepository.delete(comment);
        articleCommentCountRepository.decrease(comment.getArticleId());
        if (!comment.isRoot()) {
            commentRepository.findByPath(comment.getCommentPath().getParentPath())
                    .filter(CommentV2::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }

    public CommentPageResponseV2 readAll(Long articleId, Long page, Long pageSize) {
        List<CommentResponseV2> commentResponses = commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize)
                .stream()
                .map(CommentResponseV2::from)
                .toList();

        Long count = commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L));

        return CommentPageResponseV2.of(commentResponses, count);
    }

    public List<CommentResponseV2> readAllInfiniteScroll(Long articleId, String lastPath, Long pageSize) {
        List<CommentV2> comments = lastPath == null ?
                commentRepository.findAllInfiniteScroll(articleId, pageSize) :
                commentRepository.findAllInfiniteScroll(articleId, lastPath, pageSize);

        return comments.stream()
                .map(CommentResponseV2::from)
                .toList();
    }

    public Long count(Long articleId) {
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }

}
