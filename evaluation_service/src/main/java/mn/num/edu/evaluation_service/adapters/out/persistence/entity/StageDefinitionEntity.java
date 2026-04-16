package mn.num.edu.evaluation_service.adapters.out.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("stage_definitions")
public class StageDefinitionEntity {

    @Id
    private String stageId;

    private String workflowId;
    private String departmentId;
    private String stageName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double weight;
    private String allowedEvaluatorRolesJson;
}