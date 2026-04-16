package mn.num.edu.user_service.adapter.in.web.controller;

import jakarta.validation.Valid;
import mn.num.edu.user_service.adapter.in.web.request.LoginRequest;
import mn.num.edu.user_service.application.dto.LoginCommand;
import mn.num.edu.user_service.application.dto.LoginResponse;
import mn.num.edu.user_service.application.port.in.AuthenticateUserUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Нэвтрэлтийн controller.
 * Хэрэглэгч email/password илгээж JWT token авна.
 * Gateway дээр /api/auth/** нээлттэй (token шаардахгүй).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    /** Нэвтрэх endpoint. Email + password -> JWT token буцаана. */
    @PostMapping("/login")
    public Mono<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return authenticateUserUseCase.login(new LoginCommand(request.email(), request.password()));
    }
}
