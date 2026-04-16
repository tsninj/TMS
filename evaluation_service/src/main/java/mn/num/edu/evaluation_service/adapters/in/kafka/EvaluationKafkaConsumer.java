package mn.num.edu.evaluation_service.adapters.in.kafka;

import lombok.extern.slf4j.Slf4j;
import mn.num.edu.evaluation_service.domain.event.WorkflowStageActivatedEvent;
import mn.num.edu.evaluation_service.application.service.WorkflowStageActivationHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EvaluationKafkaConsumer {

    private final WorkflowStageActivationHandler workflowStageActivationHandler;

    public EvaluationKafkaConsumer(WorkflowStageActivationHandler workflowStageActivationHandler) {
        this.workflowStageActivationHandler = workflowStageActivationHandler;
        log.info("Evaluation Kafka consumer initialized for workflow-stage-activated");
    }

    @KafkaListener(
            topics = "workflow-stage-activated",
            groupId = "evaluation-service-group",
            containerFactory = "workflowStageActivatedKafkaListenerContainerFactory"
    )
    public void consumeWorkflowStageActivated(WorkflowStageActivatedEvent event) {
        workflowStageActivationHandler.handle(event).subscribe();
    }
}
