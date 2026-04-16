package mn.num.edu.evaluation_service.application.service;

import mn.num.edu.evaluation_service.application.port.in.HandleWorkflowStageActivatedUseCase;
import mn.num.edu.evaluation_service.application.port.out.EvaluationSlotRepositoryPort;
import mn.num.edu.evaluation_service.application.port.out.ThesisStageAssignmentRepositoryPort;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageActivatedEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WorkflowStageActivationHandler implements HandleWorkflowStageActivatedUseCase {

    private final ThesisStageAssignmentRepositoryPort assignmentRepositoryPort;
    private final EvaluationSlotRepositoryPort slotRepositoryPort;
    private final EvaluationSlotFactory slotFactory;

    public WorkflowStageActivationHandler(ThesisStageAssignmentRepositoryPort assignmentRepositoryPort,
                                          EvaluationSlotRepositoryPort slotRepositoryPort,
                                          EvaluationSlotFactory slotFactory) {
        this.assignmentRepositoryPort = assignmentRepositoryPort;
        this.slotRepositoryPort = slotRepositoryPort;
        this.slotFactory = slotFactory;
    }

    @Override
    public Mono<Void> handle(WorkflowStageActivatedEvent event) {
        return assignmentRepositoryPort.findAllByDepartmentId(event.departmentId())
                .filter(assignment -> assignment.isThesisApproved())
                .flatMap(assignment ->
                        slotRepositoryPort.existsByThesisIdAndStageId(assignment.getThesisId(), event.stageId())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.empty();
                                    }
                                    return slotRepositoryPort.save(
                                            slotFactory.open(event.stageId(), assignment, event.occurredAt())
                                    ).then();
                                })
                )
                .then();
    }
}