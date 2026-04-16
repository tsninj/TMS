package mn.num.edu.evaluation_service.application.port.in;

import mn.num.edu.evaluation_service.domain.event.ThesisApprovedEvent;
import reactor.core.publisher.Mono;

public interface HandleThesisApprovedUseCase {
    Mono<Void> handle(ThesisApprovedEvent event);
}