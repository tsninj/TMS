package mn.num.edu.user_service.exception;

/**
 * Нэвтрэлт амжилтгүй болсон үед шидэх алдаа.
 * GlobalExceptionHandler-т баригдаж 401 Unauthorized хариу буцаана.
 */
public class AuthenticationFailedException extends RuntimeException {

    private AuthenticationFailedException(String message) {
        super(message);
    }

    /** Email эсвэл нууц үг буруу үед ашиглана. */
    public static AuthenticationFailedException invalidCredentials() {
        return new AuthenticationFailedException("Invalid email or password");
    }
}
