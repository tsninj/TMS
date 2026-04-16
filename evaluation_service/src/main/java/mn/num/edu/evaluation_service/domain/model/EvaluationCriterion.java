package mn.num.edu.evaluation_service.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class EvaluationCriterion {
    private String criterionId;
    private String name;
    private BigDecimal maxPoint;

    public EvaluationCriterion(String criterionId, String name, BigDecimal maxPoint) {
        this.criterionId = criterionId;
        this.name = name;
        this.maxPoint = maxPoint;
    }

}