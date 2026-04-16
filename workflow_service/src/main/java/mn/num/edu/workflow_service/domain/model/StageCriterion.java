package mn.num.edu.workflow_service.domain.model;
import java.util.UUID;

public class StageCriterion {
    private String id;
    private String stageId;
    private String name;
    private double maxScore;
    private String description;

    public StageCriterion(String id, String stageId, String name, double maxScore, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Criterion name хоосон байж болохгүй");
        }
        if (maxScore <= 0) {
            throw new IllegalArgumentException("Criterion оноо 0-ээс их байх ёстой");
        }

        this.id = id;
        this.stageId = stageId;
        this.name = name;
        this.maxScore = maxScore;
        this.description = description;
    }

    public String getId() { return id; }
    public String getStageId() { return stageId; }
    public String getName() { return name; }
    public double getMaxScore() { return maxScore; }
    public String getDescription() { return description; }
}