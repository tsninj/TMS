package mn.num.edu.evaluation_service.domain.event;

import java.time.Instant;
import java.time.LocalDateTime;

public record ThesisApprovedEvent(
        String thesisId,
        String studentId,
        String titleMn,
        String titleEn,
        String status,
        String supervisorId,
        String committeeId,
        LocalDateTime occurredAt
) {
}
