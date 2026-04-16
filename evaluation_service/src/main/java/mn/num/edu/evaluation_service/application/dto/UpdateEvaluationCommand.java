package mn.num.edu.evaluation_service.application.dto;

import java.util.List;
import java.util.UUID;

public record UpdateEvaluationCommand(
        String evaluationId,
        List<CriterionInput> criteria
) {
    public record CriterionInput(
            String criterionId,
            Integer achievedPercent
    ) {}
}