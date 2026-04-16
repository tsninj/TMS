package mn.num.edu.evaluation_service.application.port.in;

import mn.num.edu.evaluation_service.domain.event.WorkflowStageActivatedEvent;
import reactor.core.publisher.Mono;

public interface HandleWorkflowStageActivatedUseCase {
    Mono<Void> handle(WorkflowStageActivatedEvent event);
}