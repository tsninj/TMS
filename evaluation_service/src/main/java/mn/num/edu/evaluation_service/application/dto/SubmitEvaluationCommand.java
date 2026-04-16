package mn.num.edu.evaluation_service.application.dto;

import mn.num.edu.evaluation_service.domain.model.CriterionAssessment;
import mn.num.edu.evaluation_service.domain.model.EvaluatorRole;

import java.util.List;


public class SubmitEvaluationCommand {
    private String thesisId;
    private String stageId;
    private String evaluatorId;
    private EvaluatorRole evaluatorRole;
    private List<CriterionAssessment> assessments;

    public String getThesisId() { return thesisId; }
    public void setThesisId(String thesisId) { this.thesisId = thesisId; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public String getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(String evaluatorId) { this.evaluatorId = evaluatorId; }
    public EvaluatorRole getEvaluatorRole() { return evaluatorRole; }
    public void setEvaluatorRole(EvaluatorRole evaluatorRole) { this.evaluatorRole = evaluatorRole; }
    public List<CriterionAssessment> getAssessments() { return assessments; }
    public void setAssessments(List<CriterionAssessment> assessments) { this.assessments = assessments; }
}