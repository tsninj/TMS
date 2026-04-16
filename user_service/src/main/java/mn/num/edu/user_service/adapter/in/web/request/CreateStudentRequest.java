package mn.num.edu.user_service.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mn.num.edu.user_service.domain.model.SystemRole;
@Schema(description = "Create student request")

public record CreateStudentRequest(
        @Schema(example = "Bat")
        @NotBlank String firstName,
        @Schema(example = "Bold")
        @NotBlank String lastName,
        @Schema(example = "22B1NUM0000@stud.num.edu.mn")
        @Email @NotBlank String email,
        @Schema(example = "22B1NUM0000")
        @NotBlank String studentId,
        @Schema(example = "dep-01")
        @NotBlank String departmentId,
        @Schema(example = "Computer Science")
        @NotBlank String major
) {
}