package mn.num.edu.evaluation_service.domain.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StageDefinition {
    private String stageId;
    private String workflowId;
    private String departmentId;
    private String stageName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double weight;
    private List<String> allowedEvaluatorRoles;

    public StageDefinition() {}

    public StageDefinition(String stageId, String workflowId, String departmentId, String stageName,
                           LocalDate startDate, LocalDate endDate, double weight,
                           List<String> allowedEvaluatorRoles) {
        this.stageId = stageId;
        this.workflowId = workflowId;
        this.departmentId = departmentId;
        this.stageName = stageName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weight = weight;
        this.allowedEvaluatorRoles = allowedEvaluatorRoles;
    }

}

