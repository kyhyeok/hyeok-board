package hyeok.board.comment.controller;

import hyeok.board.comment.service.CommentServiceV2;
import hyeok.board.comment.service.request.CommentCreateRequestV2;
import hyeok.board.comment.service.response.CommentResponseV2;
import org.springframework.web.bind.annotation.*;

@RestController
public record CommentControllerV2(
        CommentServiceV2 commentService
) {
    @GetMapping("/v2/comments/{commentId}")
    public CommentResponseV2 read(
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.read(commentId);
    }

    @PostMapping("/v2/comments")
    public CommentResponseV2 create(@RequestBody CommentCreateRequestV2 createRequest) {
        return commentService.create(createRequest);
    }

    @DeleteMapping("/v2/comments/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
    }

//    @GetMapping("/v2/comments")
//    public CommentPageResponse readAll(
//            @RequestParam("articleId") Long articleId,
//            @RequestParam("page") Long page,
//            @RequestParam("pageSize") Long pageSize
//    ) {
//        return commentService.readAll(articleId, page, pageSize);
//    }
//
//    @GetMapping("/v2/comments/infinite-scroll")
//    public List<CommentResponse> readAll(
//            @RequestParam("articleId") Long articleId,
//            @RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
//            @RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
//            @RequestParam("pageSize") Long pageSize
//    ) {
//        return commentService.readAll(articleId, lastParentCommentId, lastCommentId, pageSize);
//    }
}
