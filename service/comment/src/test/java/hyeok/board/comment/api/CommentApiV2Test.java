package hyeok.board.comment.api;

import hyeok.board.comment.service.request.CommentCreateRequestV2;
import hyeok.board.comment.service.response.CommentResponseV2;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponseV2 response1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponseV2 response2 = create(new CommentCreateRequestV2(1L, "my comment2", response1.path(), 1L));
        CommentResponseV2 response3 = create(new CommentCreateRequestV2(1L, "my comment3", response2.path(), 1L));

        System.out.println("response1.path() = " + response1.path());
        System.out.println("response1.commentId() = " + response1.commentId());
        System.out.println("\tresponse2.path() = " + response2.path());
        System.out.println("\tresponse2.commentId() = " + response2.commentId());
        System.out.println("\t\tresponse3.path() = " + response3.path());
        System.out.println("\t\tresponse3.commentId() = " + response3.commentId());
    }

    CommentResponseV2 create(CommentCreateRequestV2 createRequest) {
        return restClient.post()
                .uri("/v2/comments")
                .body(createRequest)
                .retrieve()
                .body(CommentResponseV2.class);
    }

    /**
     * response1.path() = 00002
     * response1.commentId() = 210033239559081984
     * 	response2.path() = 0000200000
     * 	response2.commentId() = 210033239559081984
     * 		response3.path() = 000020000000000
     * 		response3.commentId() = 210033239655550976
     */
    @Test
    void read() {
        CommentResponseV2 response = restClient.get()
                .uri("v2/comments/{commentId}", 210033239559081984L)
                .retrieve()
                .body(CommentResponseV2.class);
        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v2/comments/{commentId}", 210033239559081984L)
                .retrieve();
    }

    public record CommentCreateRequestV2(
            Long articleId,
            String content,
            String parentPath,
            Long writerId
    ) {
    }
}
