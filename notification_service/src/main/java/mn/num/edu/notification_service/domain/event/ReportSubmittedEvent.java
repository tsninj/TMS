package mn.num.edu.notification_service.domain.event;


import java.time.LocalDateTime;
import java.util.UUID;

public record ReportSubmittedEvent(
        UUID reportId,
        UUID studentId,
        String reportType,
        LocalDateTime submittedAt
) {}