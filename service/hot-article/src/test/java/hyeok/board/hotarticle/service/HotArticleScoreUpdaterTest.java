package hyeok.board.hotarticle.service;

import hyeok.board.common.event.Event;
import hyeok.board.hotarticle.repository.ArticleCreatedTimeRepository;
import hyeok.board.hotarticle.repository.HotArticleListRepository;
import hyeok.board.hotarticle.service.eventhandler.EventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreUpdaterTest {
    @InjectMocks
    HotArticleScoreUpdater hotArticleScoreUpdater;

    @Mock
    HotArticleListRepository hotArticleListRepository;

    @Mock
    HotArticleScoreCalculator hotArticleScoreCalculator;

    @Mock
    ArticleCreatedTimeRepository articleCreatedTimeRepository;

    @Test
    void updateIfArticleNotCreatedToday() {
        // given
        Long articleId = 1L;
        Event event = mock(Event.class);
        EventHandler eventHandler = mock(EventHandler.class);

        given(eventHandler.findArticleId(event)).willReturn(articleId);

        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
        given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

        // when
        hotArticleScoreUpdater.update(event, eventHandler);

        // then

        verify(eventHandler, never()).handle(event);
        verify(hotArticleListRepository, never())
                .add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
    }

    @Test
    void update() {
        // given
        Long articleId = 1L;
        Event event = mock(Event.class);
        EventHandler eventHandler = mock(EventHandler.class);

        given(eventHandler.findArticleId(event)).willReturn(articleId);

        LocalDateTime createdTime = LocalDateTime.now();
        given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

        // when
        hotArticleScoreUpdater.update(event, eventHandler);

        // then

        verify(eventHandler).handle(event);
        verify(hotArticleListRepository)
                .add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
    }
}