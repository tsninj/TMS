package mn.num.edu.evaluation_service.adapters.out.persistence.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Table("criterion_assessments")
public class CriterionAssessmentEntity {

    @Id
    private String id;
    private String evaluationId;
    private String criterionId;
    private String criterionName;
    private BigDecimal maxPoint;
    private Integer achievedPercent;
    private BigDecimal weightedScore;

    public CriterionAssessmentEntity() {}

    public CriterionAssessmentEntity(
            String id,
            String evaluationId,
            String criterionId,
            String criterionName,
            BigDecimal maxPoint,
            Integer achievedPercent,
            BigDecimal weightedScore
    ) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.criterionId = criterionId;
        this.criterionName = criterionName;
        this.maxPoint = maxPoint;
        this.achievedPercent = achievedPercent;
        this.weightedScore = weightedScore;
    }

}
