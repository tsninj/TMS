package mn.num.edu.evaluation_service.adapters.out.persistence.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Table("evaluations")
public class EvaluationEntity implements Persistable<String> {

    @Id
    private String id;
    private String thesisId;
    private String workflowId;
    private String stageId;
    private String stageName;
    private double stageMaxPoint;
    private String committeeId;
    private String evaluatorId;
    private String status;
    @Transient
    private boolean isNew = true;
    public EvaluationEntity() {}

    public EvaluationEntity(
            String id,
            String thesisId,
            String workflowId,
            String stageId,
            String stageName,
            double stageMaxPoint,
            String committeeId,
            String evaluatorId,
            String status
    ) {
        this.id = id;
        this.thesisId = thesisId;
        this.workflowId = workflowId;
        this.stageId = stageId;
        this.stageName = stageName;
        this.stageMaxPoint = stageMaxPoint;
        this.committeeId = committeeId;
        this.evaluatorId = evaluatorId;
        this.status = status;
    }
    @Override
    public boolean isNew() {
        return isNew;
    }
    public void markNotNew() {
        this.isNew = false;
    }



}