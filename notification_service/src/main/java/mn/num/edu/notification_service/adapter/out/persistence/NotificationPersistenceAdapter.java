package mn.num.edu.notification_service.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mn.num.edu.notification_service.application.port.out.LoadNotificationPort;
import mn.num.edu.notification_service.application.port.out.SaveNotificationPort;
import mn.num.edu.notification_service.domain.model.Notification;
import mn.num.edu.notification_service.domain.model.NotificationStatus;
import mn.num.edu.notification_service.domain.model.NotificationType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements SaveNotificationPort, LoadNotificationPort {

    private final NotificationR2dbcRepository repository;

    @Override
    public Mono<Notification> save(Notification notification) {
        return repository.save(toEntity(notification)).map(this::toDomain);
    }

    @Override
    public Mono<Notification> update(Notification notification) {
        return repository.save(toEntity(notification)).map(this::toDomain);
    }

    @Override
    public Mono<Notification> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Notification> findByUserId(UUID userId) {
        return repository.findByUserId(userId).map(this::toDomain);
    }

    private NotificationEntity toEntity(Notification n) {
        return NotificationEntity.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType().name())
                .status(n.getStatus().name())
                .createdAt(n.getCreatedAt())
                .sentAt(n.getSentAt())
                .build();
    }

    private Notification toDomain(NotificationEntity e) {
        return Notification.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .title(e.getTitle())
                .message(e.getMessage())
                .type(NotificationType.valueOf(e.getType()))
                .status(NotificationStatus.valueOf(e.getStatus()))
                .createdAt(e.getCreatedAt())
                .sentAt(e.getSentAt())
                .build();
    }
}