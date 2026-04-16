package mn.num.edu.evaluation_service.domain.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ThesisStageAssignment {
    private String assignmentId;
    private String thesisId;
    private String studentId;
    private String departmentId;
    private String committeeId;
    private boolean thesisApproved;

    public ThesisStageAssignment() {}

    public ThesisStageAssignment(String assignmentId, String thesisId, String studentId,
                                 String departmentId, String committeeId, boolean thesisApproved) {
        this.assignmentId = assignmentId;
        this.thesisId = thesisId;
        this.studentId = studentId;
        this.departmentId = departmentId;
        this.committeeId = committeeId;
        this.thesisApproved = thesisApproved;
    }

}