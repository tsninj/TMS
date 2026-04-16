package mn.num.edu.workflow_service.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
@Getter
public class WorkflowStage {
    private String id;
    private String workflowId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private double weightPercent;
    @Getter
    private StageStatus status;
    int stageOrder;
    private final List<StageCriterion> criteria = new ArrayList<>();
    private final Set<EvaluatorRole> allowedEvaluatorRoles = new HashSet<>();

    public WorkflowStage(
            String id,
            String workflowId,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            double weightPercent,
            StageStatus status,
            int stageOrder
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Stage name хоосон байж болохгүй");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Stage огноо заавал байна");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date start date-аас өмнө байж болохгүй");
        }
        if (weightPercent <= 0 || weightPercent > 100) {
            throw new IllegalArgumentException("Stage хувь 0-100 хооронд байна");
        }

        this.id = id;
        this.workflowId = workflowId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weightPercent = weightPercent;
        this.status = status;
        this.stageOrder = stageOrder;
    }

    public void activate() {
        if (this.status == StageStatus.CLOSED) {
            throw new IllegalStateException("Closed stage-ийг дахин идэвхжүүлж болохгүй");
        }
        this.status = StageStatus.ACTIVE;
    }

    public void close() {
        if (this.status == StageStatus.CLOSED) {
            throw new IllegalStateException("Stage аль хэдийн хаагдсан");
        }
        this.status = StageStatus.CLOSED;
    }

    public boolean isDateInRange(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate))
                && (date.isEqual(endDate) || date.isBefore(endDate));
    }
    public void addCriterion(StageCriterion criterion) {
        if (!criterion.getStageId().equals(this.id)) {
            throw new IllegalArgumentException("Criterion-ийн stageId буруу");
        }
        criteria.add(criterion);
    }

    public void addAllowedEvaluatorRole(EvaluatorRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Evaluator role хоосон байж болохгүй");
        }
        allowedEvaluatorRoles.add(role);
    }

    public boolean canBeEvaluatedBy(EvaluatorRole role) {
        return allowedEvaluatorRoles.contains(role);
    }

    public void validateCriteriaTotal() {
        double total = criteria.stream()
                .mapToDouble(StageCriterion::getMaxScore)
                .sum();

        if (Math.abs(total - this.weightPercent) > 0.0001) {
            throw new IllegalArgumentException(
                    "Criterion нийлбэр stage maxScore-тэй тэнцүү байх ёстой"
            );
        }
    }

    public void validateEvaluatorRoles() {
        if (allowedEvaluatorRoles.isEmpty()) {
            throw new IllegalArgumentException("Stage дээр дор хаяж 1 evaluator role байх ёстой");
        }
    }

}