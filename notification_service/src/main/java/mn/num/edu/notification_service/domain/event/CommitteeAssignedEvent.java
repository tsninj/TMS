package mn.num.edu.notification_service.domain.event;

import java.util.UUID;

public record CommitteeAssignedEvent(
        UUID committeeId,
        UUID teacherId,
        UUID studentId,
        String committeeName,
        String role
) {}