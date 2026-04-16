package mn.num.edu.evaluation_service.application.dto;


import mn.num.edu.evaluation_service.domain.model.EvaluationStatus;

public class EvaluationResponse {
    private String evaluationId;
    private String thesisId;
    private String stageId;
    private double totalScore;
    private EvaluationStatus status;

    public EvaluationResponse() {}

    public EvaluationResponse(String evaluationId, String thesisId, String stageId, double totalScore, EvaluationStatus status) {
        this.evaluationId = evaluationId;
        this.thesisId = thesisId;
        this.stageId = stageId;
        this.totalScore = totalScore;
        this.status = status;
    }

    public String getEvaluationId() { return evaluationId; }
    public void setEvaluationId(String evaluationId) { this.evaluationId = evaluationId; }
    public String getThesisId() { return thesisId; }
    public void setThesisId(String thesisId) { this.thesisId = thesisId; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }
    public EvaluationStatus getStatus() { return status; }
    public void setStatus(EvaluationStatus status) { this.status = status; }
}