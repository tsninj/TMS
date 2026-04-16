package mn.num.edu.evaluation_service.domain.event;

import java.time.LocalDate;
import java.util.List;

public  record WorkflowStageCreatedEvent(
        String eventId,
        String workflowId,
        String stageId,
        String departmentId,
        String stageName,
        LocalDate startDate,
        LocalDate endDate,
        double weight,
        List<String> allowedEvaluatorRoles
) {
}