package mn.num.edu.user_service.application.dto;

public record CreateDepartmentCommand(

        String firstName,
        String lastName,
        String email,
        String password,
        String departmentId,
        String departmentName

) {


}
