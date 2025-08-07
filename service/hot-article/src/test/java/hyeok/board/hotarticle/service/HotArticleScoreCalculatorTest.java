package hyeok.board.hotarticle.service;

import hyeok.board.hotarticle.repository.ArticleCommentCountRepository;
import hyeok.board.hotarticle.repository.ArticleLikeCountRepository;
import hyeok.board.hotarticle.repository.ArticleViewCountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.random.RandomGenerator;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {
    @InjectMocks
    HotArticleScoreCalculator hotArticleScoreCalculator;

    @Mock
    ArticleLikeCountRepository articleLikeCountRepository;

    @Mock
    ArticleViewCountRepository articleViewCountRepository;

    @Mock
    ArticleCommentCountRepository articleCommentCountRepository;

    @Test
    void calculate() {
        Long articleId = 1L;
        
        long likeCount = RandomGenerator.getDefault().nextLong(100);
        long commentCount = RandomGenerator.getDefault().nextLong(100);
        long viewCount = RandomGenerator.getDefault().nextLong(100);

        given(articleLikeCountRepository.read(articleId)).willReturn(likeCount);
        given(articleCommentCountRepository.read(articleId)).willReturn(commentCount);
        given(articleViewCountRepository.read(articleId)).willReturn(viewCount);

        long score = hotArticleScoreCalculator.calculate(articleId);

        Assertions.assertThat(score)
                .isEqualTo((3 * likeCount) + (2 * commentCount) + viewCount);
    }

}