package mn.num.edu.user_service.application.dto;

public record CreateTeacherCommand(

        String firstName,
        String lastName,
        String email,
        String password,
        String departmentId,
        String position

) {


}
