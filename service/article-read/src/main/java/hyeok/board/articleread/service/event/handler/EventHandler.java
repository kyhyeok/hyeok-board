package hyeok.board.articleread.service.event.handler;

import hyeok.board.common.event.Event;
import hyeok.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);

    boolean supports(Event<T> event);
}
