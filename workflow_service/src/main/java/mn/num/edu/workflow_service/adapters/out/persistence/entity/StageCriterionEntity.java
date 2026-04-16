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
@Table("stage_criteria")
public class StageCriterionEntity implements Persistable<String> {

    @Id
    private String id;
    private String stageId;
    private String name;
    private double maxScore;
    private String description;
    @Transient
    private boolean isNew = true;

    public StageCriterionEntity() {
    }

    public StageCriterionEntity(String id, String stageId, String name, double maxScore, String description) {
        this.id = id;
        this.stageId = stageId;
        this.name = name;
        this.maxScore = maxScore;
        this.description = description;
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