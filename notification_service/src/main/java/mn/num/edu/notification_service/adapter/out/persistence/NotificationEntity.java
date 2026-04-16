package mn.num.edu.notification_service.adapter.out.persistence;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("notifications")
public class NotificationEntity {
    @Id
    private UUID id;
    private UUID userId;
    private String title;
    private String message;
    private String type;
    private String channel;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}