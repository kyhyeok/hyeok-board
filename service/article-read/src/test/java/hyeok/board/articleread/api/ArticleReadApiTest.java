package hyeok.board.articleread.api;

import hyeok.board.articleread.service.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleReadApiTest {
    RestClient restClient = RestClient.create("http://localhost:9005");

    @Test
    void read() {
        ArticleReadResponse response = restClient.get()
                .uri("/v1/articles/{articleId}", 207386310811385856L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }
}
