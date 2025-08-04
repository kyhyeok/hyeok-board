package hyeok.board.comment.api;

import hyeok.board.comment.service.request.CommentCreateRequestV2;
import hyeok.board.comment.service.response.CommentPageResponseV2;
import hyeok.board.comment.service.response.CommentResponseV2;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

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

    @Test
    void readAll() {
        var response = restClient.get()
                .uri("/v2/comments?articleId=1&page=50000&pageSize=10")
                .retrieve()
                .body(CommentPageResponseV2.class);

        System.out.println("response.commentCount() = " + response.commentCount());

        for (CommentResponseV2 comment: response.comments()) {
            System.out.println("comment.commentId() = " + comment.commentId());
        }

        /**
         * comment.commentId() = 210035266651746309
         * comment.commentId() = 210035266702077963
         * comment.commentId() = 210035266702077968
         * comment.commentId() = 210035266706272256
         * comment.commentId() = 210035266706272259
         * comment.commentId() = 210035266706272263
         * comment.commentId() = 210035266706272264
         * comment.commentId() = 210035266706272267
         * comment.commentId() = 210035266706272270
         * comment.commentId() = 210035266706272273
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponseV2> response1 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponseV2>>() {
                });

        System.out.println("firstPage");

        for (CommentResponseV2 response : response1) {
            System.out.println("response.commentId() = " + response.commentId());
        }

        System.out.println("secondPage");

        String lastPath = response1.getLast().path();

        List<CommentResponseV2> response2 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath=%s".formatted(lastPath))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponseV2>>() {
                });

        for (CommentResponseV2 response : response2) {
            System.out.println("response.commentId() = " + response.commentId());
        }
    }

    @Test
    void count() {
        CommentResponseV2 commentResponse = create(new CommentCreateRequestV2(3L, "my comment 2", null, 1L));

        Long count1 = restClient.get()
                .uri("/v2/comments/articles/{articleId}/count", 3L)
                .retrieve()
                .body(Long.class);

        System.out.println("count1 = " + count1); // 1

        restClient.delete()
                .uri("/v2/comments/{commentId}", commentResponse.commentId())
                .retrieve();

        Long count2 = restClient.get()
                .uri("/v2/comments/articles/{articleId}/count", 3L)
                .retrieve()
                .body(Long.class);

        System.out.println("count2 = " + count2); // 0


    }

    public record CommentCreateRequestV2(
            Long articleId,
            String content,
            String parentPath,
            Long writerId
    ) {
    }
}
