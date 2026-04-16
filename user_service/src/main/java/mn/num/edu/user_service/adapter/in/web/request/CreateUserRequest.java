package mn.num.edu.user_service.adapter.in.web.request;

import mn.num.edu.user_service.domain.model.SystemRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull SystemRole systemRole
) {
}
