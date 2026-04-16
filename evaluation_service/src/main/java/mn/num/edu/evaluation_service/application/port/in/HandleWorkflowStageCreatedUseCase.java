package mn.num.edu.evaluation_service.application.port.in;

import reactor.core.publisher.Mono;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageCreatedEvent;

public interface HandleWorkflowStageCreatedUseCase {
    Mono<Void> handle(WorkflowStageCreatedEvent event);
}