package mn.num.edu.workflow_service.adapters.out.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import mn.num.edu.workflow_service.domain.model.WorkflowStage;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
@Getter
@Setter
@Table("workflows")
public class WorkflowEntity implements Persistable<String> {

    @Id
    private String id;
    @Getter
    private String departmentId;
    private String title;
    @Setter
    private String status;

    @Transient
    private boolean isNew = true;

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