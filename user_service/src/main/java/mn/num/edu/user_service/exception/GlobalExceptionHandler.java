package mn.num.edu.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Бүх controller-ийн алдааг төвлөрсөн хүлээн авч RFC 7807 (ProblemDetail) форматаар буцаана.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Нэвтрэлт амжилтгүй -> 401 Unauthorized */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ProblemDetail handleAuthenticationFailed(AuthenticationFailedException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setTitle("Unauthorized");
        return pd;
    }

    /** Буруу параметр -> 400 Bad Request */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Bad Request");
        return pd;
    }

    /** Бусад бүх алдаа -> 500 Internal Server Error */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleOther(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        pd.setTitle("Internal Server Error");
        return pd;
    }
}
