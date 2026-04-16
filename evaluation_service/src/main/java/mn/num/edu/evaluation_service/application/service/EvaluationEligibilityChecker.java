package mn.num.edu.evaluation_service.application.service;

import mn.num.edu.evaluation_service.domain.model.EvaluationSlot;
import mn.num.edu.evaluation_service.domain.model.StageDefinition;

public class EvaluationEligibilityChecker {

    public void validate(StageDefinition stageDefinition, EvaluationSlot slot, String evaluatorRole) {
        if (stageDefinition == null) {
            throw new IllegalArgumentException("Stage definition not found");
        }
        if (slot == null) {
            throw new IllegalArgumentException("Evaluation slot not found");
        }
        if (stageDefinition.getAllowedEvaluatorRoles() != null
                && !stageDefinition.getAllowedEvaluatorRoles().isEmpty()
                && !stageDefinition.getAllowedEvaluatorRoles().contains(evaluatorRole)) {
            throw new IllegalArgumentException("Evaluator role is not allowed for this stage");
        }
    }
}