package mn.num.edu.evaluation_service.adapters.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("criterion_assessments")
public class CriterionAssessmentEntity {

    @Id
    private String id;

    private String evaluationId;
    private String criterionId;
    private String criterionName;
    private double maxScore;
    private double givenScore;
    private double weight;
    private double weightedScore;
    private String comment;
}