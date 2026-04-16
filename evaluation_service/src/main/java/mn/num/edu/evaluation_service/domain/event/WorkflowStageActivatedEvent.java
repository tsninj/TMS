package mn.num.edu.evaluation_service.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record WorkflowStageActivatedEvent(
        String workflowId,
        String stageId,
        String stageName,
        LocalDate startDate,
        LocalDate endDate,
        double weightPercent,
        Instant occurredAt
        //        List<CriterionPayload> criteria
) {
//    public record CriterionPayload(
//            String criterionId,
//            String name,
//            BigDecimal maxPoint
//    ) {}
}