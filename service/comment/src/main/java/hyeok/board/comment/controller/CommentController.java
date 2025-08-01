package hyeok.board.comment.controller;

import hyeok.board.comment.service.CommentService;
import hyeok.board.comment.service.request.CommentCreateRequest;
import hyeok.board.comment.service.response.CommentResponse;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

@RestController
public record CommentController(
    CommentService commentService
) {
    @GetMapping("/v1comments/{commentId}")
    public CommentResponse read(
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.read(commentId);
    }

    @PostMapping("/v1comments/")
    public CommentResponse create(@RequestBody CommentCreateRequest createRequest) {
        return commentService.create(createRequest);
    }

    @DeleteMapping("/v1comments/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
    }
}
