package hyeok.board.articleread.api;

import hyeok.board.articleread.service.response.ArticleReadPageResponse;
import hyeok.board.articleread.service.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class ArticleReadApiTest {
    RestClient articleReadRestClient = RestClient.create("http://localhost:9005");
    RestClient articlerestClient = RestClient.create("http://localhost:9000");

    @Test
    void read() {
        ArticleReadResponse response = articleReadRestClient.get()
                .uri("/v1/articles/{articleId}", 207386310811385856L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void readAll() {
        ArticleReadPageResponse response1 = articleReadRestClient.get()
                .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L, 3_000L, 5))
                .retrieve()
                .body(ArticleReadPageResponse.class);
        System.out.println("response1.articleCount = " + response1.articleCount());
        for(ArticleReadResponse article: response1.articles()){
            System.out.println("article.articleId() = " + article.articleId());
        }

        ArticleReadPageResponse response2 = articlerestClient.get()
                .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L, 3_000L, 5))
                .retrieve()
                .body(ArticleReadPageResponse.class);
        System.out.println("response1.articleCount = " + response2.articleCount());
        for(ArticleReadResponse article: response2.articles()){
            System.out.println("article.articleId() = " + article.articleId());
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<ArticleReadResponse> responses1 = articleReadRestClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=%s&pageSize=%s&lastArticleId=%s".formatted(1L, 5L, 214332726262530048L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
                });

        for (ArticleReadResponse responses : responses1) {
            System.out.println("responses.articleId() = " + responses.articleId());
        }

        List<ArticleReadResponse> responses2 = articlerestClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=%s&pageSize=%s&lastArticleId=%s".formatted(1L, 5L, 214332726262530048L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
                });

        for (ArticleReadResponse responses : responses2) {
            System.out.println("responses.articleId() = " + responses.articleId());
        }
    }
}
