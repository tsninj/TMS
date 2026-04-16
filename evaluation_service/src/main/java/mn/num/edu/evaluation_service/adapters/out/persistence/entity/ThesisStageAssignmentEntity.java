package mn.num.edu.evaluation_service.adapters.out.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("thesis_stage_assignments")
public class ThesisStageAssignmentEntity {

    @Id
    private String assignmentId;

    private String thesisId;
    private String studentId;
    private String departmentId;
    private String committeeId;
    private Boolean thesisApproved;
}