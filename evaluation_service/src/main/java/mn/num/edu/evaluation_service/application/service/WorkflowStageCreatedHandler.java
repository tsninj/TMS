package mn.num.edu.evaluation_service.application.service;

import mn.num.edu.evaluation_service.application.port.in.HandleWorkflowStageCreatedUseCase;
import mn.num.edu.evaluation_service.application.port.out.StageDefinitionRepositoryPort;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageActivatedEvent;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageCreatedEvent;
import mn.num.edu.evaluation_service.application.port.out.EvaluationRepositoryPort;
import mn.num.edu.evaluation_service.domain.model.Evaluation;
import mn.num.edu.evaluation_service.domain.model.EvaluationStatus;
import mn.num.edu.evaluation_service.domain.model.StageDefinition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Service
public class WorkflowStageCreatedHandler implements HandleWorkflowStageCreatedUseCase {

    private final StageDefinitionRepositoryPort stageDefinitionRepositoryPort;

    public WorkflowStageCreatedHandler(StageDefinitionRepositoryPort stageDefinitionRepositoryPort) {
        this.stageDefinitionRepositoryPort = stageDefinitionRepositoryPort;
    }

    @Override
    public Mono<Void> handle(WorkflowStageCreatedEvent event) {
        StageDefinition definition = new StageDefinition(
                event.stageId(),
                event.workflowId(),
                event.departmentId(),
                event.stageName(),
                event.startDate(),
                event.endDate(),
                event.weight(),
                event.allowedEvaluatorRoles()
        );
        return stageDefinitionRepositoryPort.save(definition).then();
    }
}