package mn.num.edu.workflow_service.application.dto;

import mn.num.edu.workflow_service.domain.model.StageCriterion;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record WorkflowResponse(
        String workflowId,
        String departmentId,
        String title,
        String status,
        List<StageItem> stages
) {
    public record StageItem(
            String stageId,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            double weightPercent,
            String status,
            List<String> allowedEvaluatorRoles,
            List<CriterionItem> criteria
    ) {

    }
    public record CriterionItem(
            String criterionId,
            String name,
            double maxScore,
            String description
    ) {}
}