package mn.num.edu.evaluation_service.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Evaluation {
    private String id;
    private String thesisId;
    private String workflowId;
    private String stageId;
    private String stageName;
    private double stageMaxPoint;
    private String committeeId;
    private String evaluatorId;
    private EvaluationStatus status;
    private final List<CriterionAssessment> criteria = new ArrayList<>();

    public Evaluation(
            String id,
            String thesisId,
            String workflowId,
            String stageId,
            String stageName,
            double stageMaxPoint,
            String committeeId,
            String evaluatorId,
            EvaluationStatus status
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

    public void addCriterion(CriterionAssessment assessment) {
        this.criteria.add(assessment);
    }

    public void submit() {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("Evaluation must contain at least one criterion");
        }
        this.status = EvaluationStatus.SUBMITTED;
    }

    public void complete() {
        this.status = EvaluationStatus.COMPLETED;
    }

    public BigDecimal totalWeightedScore() {
        return criteria.stream()
                .map(CriterionAssessment::getWeightedScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}