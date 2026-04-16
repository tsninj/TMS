package mn.num.edu.user_service.exception;

public class AuthenticationFailedException extends RuntimeException {

    private AuthenticationFailedException(String message) {
        super(message);
    }

    public static AuthenticationFailedException invalidCredentials() {
        return new AuthenticationFailedException("Invalid email or password");
    }
}
