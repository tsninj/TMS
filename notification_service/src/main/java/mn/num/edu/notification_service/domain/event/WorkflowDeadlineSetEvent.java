package mn.num.edu.notification_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkflowDeadlineSetEvent(
        UUID workflowId,
        UUID thesisId,
        UUID studentId,
        String stageName,
        LocalDateTime deadline
) {}