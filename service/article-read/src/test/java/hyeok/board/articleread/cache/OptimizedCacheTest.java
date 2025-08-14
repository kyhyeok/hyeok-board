package hyeok.board.articleread.cache;

import lombok.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class OptimizedCacheTest {
    @Test
    void parseData() {
        parseData("data", 10);
        parseData(3L, 10);
        parseData(3, 10);
        parseData(new TestClass("hihi"), 10);
    }

    void parseData(Object data, long ttlSeconds) {
        // given
        OptimizedCache optimizedCache = OptimizedCache.of(data, Duration.ofSeconds(ttlSeconds));
        System.out.println("optimizedCache = " + optimizedCache);

        // when
        Object resolvedData = optimizedCache.parseData(data.getClass());
        System.out.println("resolvedData = " + resolvedData);

        // then
        assertThat(resolvedData).isEqualTo(data);
    }

    @Test
    void isExpired() {
        assertThat(OptimizedCache.of("data", Duration.ofDays(-30)).isExpired()).isTrue();
        assertThat(OptimizedCache.of("data", Duration.ofDays(30)).isExpired()).isFalse();
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestClass {
        String testData;
    }

}