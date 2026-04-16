package mn.num.edu.workflow_service.application.dto;

public record CreateStageCriterionCommand(
        String name,
        double maxScore,
        String description
) {}