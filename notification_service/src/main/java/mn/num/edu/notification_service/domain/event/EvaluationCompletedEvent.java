package mn.num.edu.notification_service.domain.event;

import java.util.UUID;

public record EvaluationCompletedEvent(
        UUID evaluationId,
        UUID studentId,
        UUID thesisId,
        String stageName,
        Double totalScore
) {}