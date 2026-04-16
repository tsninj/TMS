package mn.num.edu.evaluation_service.adapters.in.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.num.edu.evaluation_service.application.port.in.*;
import mn.num.edu.evaluation_service.domain.event.ThesisApprovedEvent;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageActivatedEvent;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EvaluationKafkaConsumer {

    private final HandleWorkflowStageCreatedUseCase handleWorkflowStageCreatedUseCase;
    private final HandleWorkflowStageActivatedUseCase handleWorkflowStageActivatedUseCase;
    private final HandleThesisApprovedUseCase handleThesisApprovedUseCase;

    @KafkaListener(
            topics = "workflow.stage.created",
            groupId = "evaluation-service-group",
            containerFactory = "workflowStageCreatedKafkaListenerContainerFactory"
    )
    public void consumeWorkflowStageCreated(WorkflowStageCreatedEvent event) {
        handleWorkflowStageCreatedUseCase.handle(event).subscribe();
    }

    @KafkaListener(
            topics = "workflow.stage.activated",
            groupId = "evaluation-service-group",
            containerFactory = "workflowStageActivatedKafkaListenerContainerFactory"
    )
    public void consumeWorkflowStageActivated(WorkflowStageActivatedEvent event) {
        handleWorkflowStageActivatedUseCase.handle(event).subscribe();
    }

    @KafkaListener(
            topics = "thesis.approved",
            groupId = "evaluation-service-group",
            containerFactory = "thesisApprovedKafkaListenerContainerFactory"
    )
    public void consumeThesisApproved(ThesisApprovedEvent event) {
        handleThesisApprovedUseCase.handle(event).subscribe();
    }


}