package mn.num.edu.user_service.application.dto;

public record CreateStudentCommand(

        String firstName,
        String lastName,
        String email,
        String password,
        String studentId,
        String departmentId,
        String major

) {


}
