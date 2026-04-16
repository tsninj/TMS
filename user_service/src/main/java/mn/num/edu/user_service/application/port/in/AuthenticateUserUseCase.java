package mn.num.edu.user_service.application.port.in;

import mn.num.edu.user_service.application.dto.LoginCommand;
import mn.num.edu.user_service.application.dto.LoginResponse;
import reactor.core.publisher.Mono;

public interface AuthenticateUserUseCase {
    Mono<LoginResponse> login(LoginCommand command);
}
