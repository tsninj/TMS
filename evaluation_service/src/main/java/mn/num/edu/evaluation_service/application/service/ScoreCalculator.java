package mn.num.edu.evaluation_service.application.service;

import mn.num.edu.evaluation_service.domain.model.CriterionAssessment;

import java.util.List;

public class ScoreCalculator {

    public double calculateTotalScore(List<CriterionAssessment> assessments) {
        double total = assessments.stream()
                .mapToDouble(CriterionAssessment::getWeightedScore)
                .sum();
        return round(total);
    }

    public double calculateWeighted(double givenScore, double maxScore, double weight) {
        if (maxScore == 0) {
            return 0.0;
        }
        double weighted = (givenScore * weight) / maxScore;
        return round(weighted);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

