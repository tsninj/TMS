package mn.num.edu.notification_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ThesisApprovedEvent(
        UUID thesisId,
        UUID studentId,
        String thesisTitle,
        LocalDateTime approvedAt
) {}