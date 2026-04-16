package mn.num.edu.evaluation_service.domain.event;
import java.time.Instant;

public record EvaluationCompletedEvent(
        String evaluationId,
        String thesisId,
        String stageId,
        String committeeId,
        double totalScore,
        Instant completedAt
) {
}