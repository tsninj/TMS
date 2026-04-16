package mn.num.edu.evaluation_service.domain.event;

import java.math.BigDecimal;
import java.util.List;

public record WorkflowStageActivatedEvent(
        String workflowId,
        String thesisId,
        String stageId,
        String stageName,
        BigDecimal stageMaxPoint,
        List<CriterionPayload> criteria
) {
    public record CriterionPayload(
            String criterionId,
            String name,
            BigDecimal maxPoint
    ) {}
}