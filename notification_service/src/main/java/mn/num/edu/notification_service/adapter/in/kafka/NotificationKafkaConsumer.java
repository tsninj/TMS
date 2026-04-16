package mn.num.edu.notification_service.adapter.in.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.num.edu.notification_service.application.port.in.CreateNotificationUseCase;
import mn.num.edu.notification_service.domain.event.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final CreateNotificationUseCase createNotificationUseCase;

    @KafkaListener(topics = "thesis.approved", groupId = "notification-service-group")
    public void consumeThesisApproved(ThesisApprovedEvent event) {
        log.info("Consumed ThesisApprovedEvent: {}", event);
        createNotificationUseCase.handleThesisApproved(event).subscribe();
    }

    @KafkaListener(topics = "committee.assigned", groupId = "notification-service-group")
    public void consumeCommitteeAssigned(CommitteeAssignedEvent event) {
        log.info("Consumed CommitteeAssignedEvent: {}", event);
        createNotificationUseCase.handleCommitteeAssigned(event).subscribe();
    }

    @KafkaListener(topics = "report.submitted", groupId = "notification-service-group")
    public void consumeReportSubmitted(ReportSubmittedEvent event) {
        log.info("Consumed ReportSubmittedEvent: {}", event);
        createNotificationUseCase.handleReportSubmitted(event).subscribe();
    }

    @KafkaListener(topics = "evaluation.completed", groupId = "notification-service-group")
    public void consumeEvaluationCompleted(EvaluationCompletedEvent event) {
        log.info("Consumed EvaluationCompletedEvent: {}", event);
        createNotificationUseCase.handleEvaluationCompleted(event).subscribe();
    }

    @KafkaListener(topics = "final.grade.calculated", groupId = "notification-service-group")
    public void consumeFinalGradeCalculated(FinalGradeCalculatedEvent event) {
        log.info("Consumed FinalGradeCalculatedEvent: {}", event);
        createNotificationUseCase.handleFinalGradeCalculated(event).subscribe();
    }

    @KafkaListener(topics = "workflow.deadline.set", groupId = "notification-service-group")
    public void consumeWorkflowDeadlineSet(WorkflowDeadlineSetEvent event) {
        log.info("Consumed WorkflowDeadlineSetEvent: {}", event);
        createNotificationUseCase.handleWorkflowDeadlineSet(event).subscribe();
    }
}