package mn.num.edu.grading_service.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Resolution {
    private UUID id;
    private UUID workflowId;
    private String title;
    private int totalStudents;
    private int passedStudents;
    private int failedStudents;
    private LocalDateTime generatedAt;
    private List<ResolutionStudentResult> studentResults;

    public Resolution(
            UUID id,
            UUID workflowId,
            String title,
            int totalStudents,
            int passedStudents,
            int failedStudents,
            LocalDateTime generatedAt,
            List<ResolutionStudentResult> studentResults
    ) {
        this.id = id;
        this.workflowId = workflowId;
        this.title = title;
        this.totalStudents = totalStudents;
        this.passedStudents = passedStudents;
        this.failedStudents = failedStudents;
        this.generatedAt = generatedAt;
        this.studentResults = studentResults;
    }

    public static Resolution create(
            UUID workflowId,
            String title,
            int totalStudents,
            int passedStudents,
            int failedStudents,
            List<ResolutionStudentResult> studentResults
    ) {
        return new Resolution(
                UUID.randomUUID(),
                workflowId,
                title,
                totalStudents,
                passedStudents,
                failedStudents,
                LocalDateTime.now(),
                studentResults
        );
    }

}