package mn.num.edu.evaluation_service.application.service;


import mn.num.edu.evaluation_service.domain.model.EvaluationSlot;
import mn.num.edu.evaluation_service.domain.model.StageStatus;
import mn.num.edu.evaluation_service.domain.model.ThesisStageAssignment;

import java.time.Instant;
import java.util.UUID;

public class EvaluationSlotFactory {

    public EvaluationSlot open(String stageId, ThesisStageAssignment assignment, Instant openedAt) {
        return new EvaluationSlot(
                UUID.randomUUID().toString(),
                assignment.getThesisId(),
                stageId,
                assignment.getDepartmentId(),
                assignment.getCommitteeId(),
                StageStatus.ACTIVE,
                openedAt,
                null
        );
    }
}