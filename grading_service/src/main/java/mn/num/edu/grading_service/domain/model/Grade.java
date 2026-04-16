package mn.num.edu.grading_service.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Grade {
    private String id;
    private String thesisId;
    private String studentId;
    private String workflowId;

    private Double processScore;
    private Double defenseScore;
    private Double finalScore;

    private GradeStatus status;
    private Boolean published;

    private LocalDateTime calculatedAt;
    private LocalDateTime publishedAt;

    public Grade(
            String id,
            String thesisId,
            String studentId,
            String workflowId,
            Double processScore,
            Double defenseScore,
            Double finalScore,
            GradeStatus status,
            Boolean published,
            LocalDateTime calculatedAt,
            LocalDateTime publishedAt
    ) {
        this.id = id;
        this.thesisId = thesisId;
        this.studentId = studentId;
        this.workflowId = workflowId;
        this.processScore = processScore;
        this.defenseScore = defenseScore;
        this.finalScore = finalScore;
        this.status = status;
        this.published = published;
        this.calculatedAt = calculatedAt;
        this.publishedAt = publishedAt;
    }

    public static Grade create(String thesisId, String studentId, String workflowId) {
        return new Grade(
                UUID.randomUUID().toString(),
                thesisId,
                studentId,
                workflowId,
                0.0,
                0.0,
                0.0,
                GradeStatus.PENDING,
                false,
                null,
                null
        );
    }

    public void updateScores(Double processScore, Double defenseScore, Double finalScore, GradeStatus status) {
        this.processScore = processScore;
        this.defenseScore = defenseScore;
        this.finalScore = finalScore;
        this.status = status;
        this.calculatedAt = LocalDateTime.now();
    }

    public void publish() {
        this.published = true;
        this.publishedAt = LocalDateTime.now();
    }

}