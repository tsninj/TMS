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
        log.info("Recieved event from Kafka workflow.stage.created");
    }


}