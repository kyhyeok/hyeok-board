package hyeok.board.comment.api;

import hyeok.board.comment.service.response.CommentPageResponse;
import hyeok.board.comment.service.response.CommentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = commentResponse(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = commentResponse(new CommentCreateRequest(1L, "my comment2", response1.commentId(), 1L));
        CommentResponse response3 = commentResponse(new CommentCreateRequest(1L, "my comment3", response1.commentId(), 1L));

        System.out.println("commentId=%s".formatted(response1.commentId()));
        System.out.println("\tcommentId=%s".formatted(response2.commentId()));
        System.out.println("\tcommentId=%s".formatted(response3.commentId()));
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 209913238322397184L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        //        commentId=209913238322397184 - x
        //            commentId=209913239329030144 - x
        //            commentId=209913239370973184 - x
        restClient.delete()
                .uri("/v1/comments/{commentId}", 209913239370973184L)
                .retrieve();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&page=1&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.commentCount() = " + response.commentCount());
        for (CommentResponse comment : response.comments()) {
            if (!comment.commentId().equals(comment.parentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.commentId());
        }
    }


    /**
     * 1번 페이지 수행 결과
     * comment.getCommentId() = 209915834304126976
     *  comment.getCommentId() = 209915834333487130
     *  comment.getCommentId() = 209915834304126977
     * comment.getCommentId() = 209915834333487167
     *  comment.getCommentId() = 209915834304126978
     * comment.getCommentId() = 209915834333487106
     *  comment.getCommentId() = 209915834304126979
     * comment.getCommentId() = 209915834333487107
     *  comment.getCommentId() = 209915834304126980
     * comment.getCommentId() = 209915834337681467
     */

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse comment : response1) {
            if (!comment.commentId().equals(comment.parentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment1.getCommentId() = " + comment.commentId());
        }

        Long lastParentCommentId = response1.getLast().parentCommentId();
        Long lastCommentId = response1.getLast().commentId();

        List<CommentResponse> response2 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=%s&lastCommentId=%s"
                        .formatted(lastParentCommentId, lastCommentId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        for (CommentResponse comment : response2) {
            if (!comment.commentId().equals(comment.parentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment2.getCommentId() = " + comment.commentId());
        }
    }

    CommentResponse commentResponse(CommentCreateRequest createRequest) {
        return restClient.post()
                .uri("/v1/comments")
                .body(createRequest)
                .retrieve()
                .body(CommentResponse.class);
    }

    public record CommentCreateRequest(
            Long articleId,
            String content,
            Long parentCommentId,
            Long writerId
    ) {
    }
}
