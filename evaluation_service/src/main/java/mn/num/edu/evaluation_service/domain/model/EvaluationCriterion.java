package mn.num.edu.evaluation_service.domain.model;

import lombok.Getter;

@Getter
public class EvaluationCriterion {
    private String criterionId;
    private String name;
    private double  maxPoint;
    private double weight;
    public EvaluationCriterion(String criterionId, String name, double maxPoint,  double weight) {
        this.criterionId = criterionId;
        this.name = name;
        this.maxPoint = maxPoint;
        this.weight = weight;
    }

}