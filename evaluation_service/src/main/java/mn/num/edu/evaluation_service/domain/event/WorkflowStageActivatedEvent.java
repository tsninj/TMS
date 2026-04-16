package mn.num.edu.evaluation_service.domain.event;

import java.time.Instant;
import java.time.LocalDate;

public record WorkflowStageActivatedEvent(
        String workflowId,
        String stageId,
        String stageName,
        String departmentId,
        LocalDate startDate,
        LocalDate endDate,
        double weightPercent,
        Instant occurredAt
) {
}