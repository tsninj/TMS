package mn.num.edu.evaluation_service.adapters.out.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("evaluation_slots")
public class EvaluationSlotEntity {

    @Id
    private String slotId;

    private String thesisId;
    private String stageId;
    private String departmentId;
    private String committeeId;
    private String status;
    private Instant openedAt;
    private Instant closedAt;
}