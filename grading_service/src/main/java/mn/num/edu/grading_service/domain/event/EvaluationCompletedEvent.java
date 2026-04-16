package mn.num.edu.grading_service.domain.event;

import java.time.LocalDateTime;

public record EvaluationCompletedEvent (
     String evaluationId,
     String thesisId,
     String studentId,
     String workflowId,
     String stageId,
     Double stageScore,
     LocalDateTime occurredAt
){

}