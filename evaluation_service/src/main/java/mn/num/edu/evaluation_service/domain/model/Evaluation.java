package mn.num.edu.evaluation_service.domain.model;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Evaluation {
    private String evaluationId;
    private String thesisId;
    private String stageId;
    private String committeeId;
    private String evaluatorId;
    @Setter
    private EvaluatorRole evaluatorRole;
    private List<CriterionAssessment> assessments = new ArrayList<>();
    private double totalScore;
    private EvaluationStatus status;
    private Instant submittedAt;

    public Evaluation() {}

    public Evaluation(String evaluationId, String thesisId, String stageId, String committeeId,
                      String evaluatorId, EvaluatorRole evaluatorRole,
                      List<CriterionAssessment> assessments, double totalScore,
                      EvaluationStatus status, Instant submittedAt) {
        this.evaluationId = evaluationId;
        this.thesisId = thesisId;
        this.stageId = stageId;
        this.committeeId = committeeId;
        this.evaluatorId = evaluatorId;
        this.evaluatorRole = evaluatorRole;
        this.assessments = assessments;
        this.totalScore = totalScore;
        this.status = status;
        this.submittedAt = submittedAt;
    }

}