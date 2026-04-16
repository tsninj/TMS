package mn.num.edu.evaluation_service.domain.event;

import java.math.BigDecimal;

public record EvaluationSubmittedEvent(
        String evaluationId,
        String thesisId,
        String stageId,
        String evaluatorId,
        BigDecimal totalScore
) {}