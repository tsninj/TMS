package mn.num.edu.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.num.edu.notification_service.application.port.in.CreateNotificationUseCase;
import mn.num.edu.notification_service.application.port.out.PublishNotificationEventPort;
import mn.num.edu.notification_service.application.port.out.SaveNotificationPort;
import mn.num.edu.notification_service.domain.event.*;
import mn.num.edu.notification_service.domain.model.Notification;
import mn.num.edu.notification_service.domain.model.NotificationStatus;
import mn.num.edu.notification_service.domain.model.NotificationType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationApplicationService implements CreateNotificationUseCase {

    private final SaveNotificationPort saveNotificationPort;
    private final PublishNotificationEventPort publishNotificationEventPort;

    @Override
    public Mono<Void> handleThesisApproved(ThesisApprovedEvent event) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(event.studentId())
                .title("Дипломын ажил батлагдлаа")
                .message("Таны \"" + event.thesisTitle() + "\" сэдэв батлагдлаа.")
                .type(NotificationType.THESIS_APPROVED)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return saveAndPublish(notification);
    }

    @Override
    public Mono<Void> handleCommitteeAssigned(CommitteeAssignedEvent event) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(event.teacherId())
                .title("Комисст томилогдлоо")
                .message("Та \"" + event.committeeName() + "\" комисст " + event.role() + " үүрэгтэй томилогдлоо.")
                .type(NotificationType.COMMITTEE_ASSIGNED)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return saveAndPublish(notification);
    }

    @Override
    public Mono<Void> handleReportSubmitted(ReportSubmittedEvent event) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(event.studentId())
                .title("Тайлан амжилттай илгээгдлээ")
                .message("Таны " + event.reportType() + " тайлан амжилттай бүртгэгдлээ.")
                .type(NotificationType.REPORT_SUBMITTED)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return saveAndPublish(notification);
    }

    @Override
    public Mono<Void> handleEvaluationCompleted(EvaluationCompletedEvent event) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(event.studentId())
                .title("Үнэлгээ дууслаа")
                .message(event.stageName() + " шатны үнэлгээ дууслаа. Нийт оноо: " + event.totalScore())
                .type(NotificationType.EVALUATION_COMPLETED)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return saveAndPublish(notification);
    }

    @Override
    public Mono<Void> handleFinalGradeCalculated(FinalGradeCalculatedEvent event) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(event.studentId())
                .title("Эцсийн дүн бодогдлоо")
                .message("Таны эцсийн үнэлгээ: " + event.finalScore() + " (" + event.letterGrade() + ")")
                .type(NotificationType.FINAL_GRADE_CALCULATED)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return saveAndPublish(notification);
    }

    @Override
    public Mono<Void> handleWorkflowDeadlineSet(WorkflowDeadlineSetEvent event) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(event.studentId())
                .title("Шатны deadline тохируулагдлаа")
                .message(event.stageName() + " шатны deadline: " + event.deadline())
                .type(NotificationType.WORKFLOW_DEADLINE_SET)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return saveAndPublish(notification);
    }

    private Mono<Void> saveAndPublish(Notification notification) {
        return saveNotificationPort.save(notification)
                .flatMap(saved -> {
                    saved.markSent();
                    return saveNotificationPort.update(saved)
                            .then(publishNotificationEventPort.publishSent(
                                    new NotificationSentEvent(
                                            saved.getId(),
                                            saved.getUserId(),
                                            saved.getTitle(),
                                            saved.getSentAt()
                                    )
                            ));
                })
                .doOnSuccess(v -> log.info("Notification processed successfully: {}", notification.getTitle()))
                .onErrorResume(ex -> {
                    log.error("Failed to process notification: {}", ex.getMessage(), ex);
                    return publishNotificationEventPort.publishFailed(
                            new NotificationFailedEvent(
                                    notification.getId(),
                                    notification.getUserId(),
                                    ex.getMessage(),
                                    LocalDateTime.now()
                            )
                    );
                });
    }
}