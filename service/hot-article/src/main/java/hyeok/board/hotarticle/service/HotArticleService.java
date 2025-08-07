package hyeok.board.hotarticle.service;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventPayload;
import hyeok.board.common.event.EventType;
import hyeok.board.hotarticle.client.ArticleClient;
import hyeok.board.hotarticle.repository.HotArticleListRepository;
import hyeok.board.hotarticle.service.eventhandler.EventHandler;
import hyeok.board.hotarticle.service.response.HotArticleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotArticleService {
    private final ArticleClient articleClient;

    private final List<EventHandler> eventHandlers;

    private final HotArticleScoreUpdater hotArticleScoreUpdater;

    private final HotArticleListRepository hotArticleListRepository;

    public void handleEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);

        if (eventHandler == null) {
            return;
        }

        if (isArticleCreateOrDeleted(event)) {
            eventHandler.handle(event);
        } else {
            hotArticleScoreUpdater.update(event, eventHandler);
        }
    }

    private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElse(null);
    }

    private boolean isArticleCreateOrDeleted(Event<EventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
    }

    public List<HotArticleResponse> readAll(String dateSrt) {
        return hotArticleListRepository.readAll(dateSrt)
                .stream()
                .map(articleClient::read)
                .filter(Objects::nonNull)
                .map(HotArticleResponse::from)
                .toList();
    }
}
