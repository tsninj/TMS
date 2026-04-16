package mn.num.edu.evaluation_service.domain.model;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class EvaluationSlot {
    private String slotId;
    private String thesisId;
    private String stageId;
    private String departmentId;
    private String committeeId;
    private StageStatus status;
    private Instant openedAt;
    private Instant closedAt;

    public EvaluationSlot() {}

    public EvaluationSlot(String slotId, String thesisId, String stageId, String departmentId,
                          String committeeId, StageStatus status, Instant openedAt, Instant closedAt) {
        this.slotId = slotId;
        this.thesisId = thesisId;
        this.stageId = stageId;
        this.departmentId = departmentId;
        this.committeeId = committeeId;
        this.status = status;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }

}

