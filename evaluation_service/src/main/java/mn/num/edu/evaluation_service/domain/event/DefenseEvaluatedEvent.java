package mn.num.edu.evaluation_service.domain.event;

import java.math.BigDecimal;

public record DefenseEvaluatedEvent(
        String thesisId,
        String stageId,
        BigDecimal defenseScore
) {}