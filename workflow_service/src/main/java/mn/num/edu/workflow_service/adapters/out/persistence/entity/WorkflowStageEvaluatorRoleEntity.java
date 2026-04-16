package mn.num.edu.workflow_service.adapters.out.persistence.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Setter
@Getter
@Table("workflow_stage_evaluator_roles")
public class WorkflowStageEvaluatorRoleEntity implements Persistable<String> {

    @Id
    private String id;
    private String stageId;
    private String evaluatorRole;
    @Transient
    private boolean isNew = true;

    public WorkflowStageEvaluatorRoleEntity() {
    }

    public WorkflowStageEvaluatorRoleEntity(String id, String stageId, String evaluatorRole) {
        this.id = id;
        this.stageId = stageId;
        this.evaluatorRole = evaluatorRole;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }

}