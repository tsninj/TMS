package mn.num.edu.notification_service.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.num.edu.notification_service.application.port.out.PublishNotificationEventPort;
import mn.num.edu.notification_service.domain.event.NotificationFailedEvent;
import mn.num.edu.notification_service.domain.event.NotificationSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaProducerAdapter implements PublishNotificationEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Mono<Void> publishSent(NotificationSentEvent event) {
        return Mono.fromFuture(kafkaTemplate.send("notification.sent", event))
                .doOnSuccess(result -> log.info("Published NotificationSentEvent: {}", event.notificationId()))
                .then();
    }

    @Override
    public Mono<Void> publishFailed(NotificationFailedEvent event) {
        return Mono.fromFuture(kafkaTemplate.send("notification.failed", event))
                .doOnSuccess(result -> log.info("Published NotificationFailedEvent: {}", event.notificationId()))
                .then();
    }
}