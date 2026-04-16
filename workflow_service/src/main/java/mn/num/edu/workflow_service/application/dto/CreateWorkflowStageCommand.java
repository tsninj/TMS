package mn.num.edu.workflow_service.application.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateWorkflowStageCommand(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        double weightPercent,
        int stageOrder,
        List<CreateStageCriterionCommand> criteria,
        List<String> allowedEvaluatorRoles


) {
}
