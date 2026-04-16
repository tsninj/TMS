package mn.num.edu.evaluation_service.domain.event;

public record EvaluationCompletedEvent(
        String evaluationId,
        String thesisId,
        String studentId,
        String workflowId,
        String stageId,
        String stageName,
        Double totalScore,
        java.time.LocalDateTime occurredAt
) {}
