package mn.num.edu.evaluation_service.adapters.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("evaluations")
public class EvaluationEntity {

    @Id
    private String evaluationId;

    private String thesisId;
    private String stageId;
    private String committeeId;
    private String evaluatorId;
    private String evaluatorRole;
    private double totalScore;
    private String status;
    private Instant submittedAt;
}