package mn.num.edu.user_service.application.dto;

import mn.num.edu.user_service.domain.model.SystemRole;

public record CreateUserCommand(

        String firstName,
        String lastName,
        String email,
        String password,
        SystemRole systemRole,
        String departmentId

) {


}
