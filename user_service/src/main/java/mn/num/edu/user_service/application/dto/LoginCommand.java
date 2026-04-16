package mn.num.edu.user_service.application.dto;

/** Нэвтрэлтийн use case руу дамжуулах command. Email болон нууц үг агуулна. */
public record LoginCommand(
        String email,
        String password
) {
}
