package mn.num.edu.evaluation_service.domain.event;

import java.math.BigDecimal;
public record EvaluationUpdatedEvent(
        String evaluationId,
        String thesisId,
        String stageId,
        BigDecimal totalScore
) {}