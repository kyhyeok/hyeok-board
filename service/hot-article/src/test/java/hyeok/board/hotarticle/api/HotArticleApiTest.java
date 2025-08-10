package hyeok.board.hotarticle.api;

import hyeok.board.hotarticle.service.response.HotArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class HotArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9004");

    @Test
    void readAll() {
        List<HotArticleResponse> responses = restClient.get()
                .uri("v1/hot-articles/articles/date/{dateStr}", "20250810")
                .retrieve()
                .body(new ParameterizedTypeReference<List<HotArticleResponse>>() {
                });

        for (HotArticleResponse response : responses) {
            System.out.println("response = " + response.toString());
        }
    }

}
