package mn.num.edu.workflow_service.adapters.out.kafka;

import mn.num.edu.workflow_service.application.port.out.WorkflowEventPublisherPort;
import mn.num.edu.workflow_service.domain.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class WorkflowKafkaProducer implements WorkflowEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(WorkflowKafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public WorkflowKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishWorkflowCreated(WorkflowCreatedEvent event) {
        kafkaTemplate.send("workflow-created", event);
        log.info("workflow-created event");
    }

    @Override
    public void publishWorkflowStageCreated(WorkflowStageCreatedEvent event) {
        kafkaTemplate.send("workflow-stage-created", event);
        log.info("workflow-stage-created event");
    }

    @Override
    public void publishWorkflowStageActivated(WorkflowStageActivatedEvent event) {
        kafkaTemplate.send("workflow-stage-activated", event);
        log.info("workflow-stage-activated event");
    }

    @Override
    public void publishWorkflowStageClosed(WorkflowStageClosedEvent event) {
        kafkaTemplate.send("workflow-stage-closed", event);
        log.info("workflow-stage-closed event");
    }

    @Override
    public void publishWorkflowDeadlineApproaching(WorkflowDeadlineApproachingEvent event) {
        kafkaTemplate.send("workflow-deadline-approaching", event);
        log.info("workflow-deadline-approaching event");
    }

    @Override
    public void publishWorkflowCompleted(WorkflowCompletedEvent event) {
        kafkaTemplate.send("workflow-completed", event);
        log.info("workflow-completed event");
    }
}
