package mn.num.edu.evaluation_service.application.dto;

import java.util.List;

public record SubmitEvaluationCommand(
        String evaluationId,
        List<CriterionInput> criteria
) {
    public record CriterionInput(
            String criterionId,
            Integer achievedPercent
    ) {}
}