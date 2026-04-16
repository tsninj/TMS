package mn.num.edu.workflow_service.adapters.out.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
@Setter
@Getter
@Table("workflow_stages")
public class WorkflowStageEntity implements Persistable<String> {

    @Id
    private String id;

    @Setter
    @Column("workflow_id")
    private String workflowId;

    private String name;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("weight_percent")
    private double weightPercent;

    private String status;

    @Getter
    @Column("stage_order")
    private int stageOrder;

    @Transient
    private boolean isNew ;

    public WorkflowStageEntity() {
    }

    public WorkflowStageEntity(String id, String workflowId, String name,
                               LocalDate startDate, LocalDate endDate,
                               double weightPercent, String status, int stageOrder) {
        this.id = id;
        this.workflowId = workflowId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weightPercent = weightPercent;
        this.status = status;
        this.stageOrder = stageOrder;
        this.isNew = true;
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