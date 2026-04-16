package mn.num.edu.evaluation_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriterionAssessment {
    private String criterionId;
    private String criterionName;
    private double maxScore;
    private double givenScore;
    private double weight;
    private double weightedScore;
    private String comment;

    public CriterionAssessment() {}

    public CriterionAssessment(String criterionId, String criterionName, double maxScore,
                               double givenScore, double weight, double weightedScore,
                               String comment) {
        this.criterionId = criterionId;
        this.criterionName = criterionName;
        this.maxScore = maxScore;
        this.givenScore = givenScore;
        this.weight = weight;
        this.weightedScore = weightedScore;
        this.comment = comment;
    }

    public void setCriterionId(String criterionId) { this.criterionId = criterionId; }

    public void setCriterionName(String criterionName) { this.criterionName = criterionName; }

    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

    public void setGivenScore(double givenScore) { this.givenScore = givenScore; }

    public void setWeight(double weight) { this.weight = weight; }

    public void setWeightedScore(double weightedScore) { this.weightedScore = weightedScore; }

    public void setComment(String comment) { this.comment = comment; }
}