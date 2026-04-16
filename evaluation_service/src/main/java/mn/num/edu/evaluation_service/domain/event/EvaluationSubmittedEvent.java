package mn.num.edu.evaluation_service.domain.event;

import java.time.Instant;

public record EvaluationSubmittedEvent(
        String evaluationId,
        String thesisId,
        String stageId,
        String committeeId,
        String evaluatorId,
        double totalScore,
        Instant submittedAt
) {}