package mn.num.edu.evaluation_service.application.dto;

import mn.num.edu.evaluation_service.domain.model.CriterionAssessment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateEvaluationCommand {
    private String evaluationId;
    private List<CriterionAssessment> assessments;

    public UpdateEvaluationCommand() {}

    public UpdateEvaluationCommand(String evaluationId, List<CriterionAssessment> assessments) {
        this.evaluationId = evaluationId;
        this.assessments = assessments;
    }
}