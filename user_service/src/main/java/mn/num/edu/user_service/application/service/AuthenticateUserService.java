package mn.num.edu.user_service.application.service;

import mn.num.edu.user_service.application.dto.LoginCommand;
import mn.num.edu.user_service.application.dto.LoginResponse;
import mn.num.edu.user_service.application.port.in.AuthenticateUserUseCase;
import mn.num.edu.user_service.application.port.out.UserRepositoryPort;
import mn.num.edu.user_service.domain.model.User;
import mn.num.edu.user_service.exception.AuthenticationFailedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Нэвтрэлтийн бизнес логик.
 * Email-аар хэрэглэгч хайж, нууц үг BCrypt-ээр шалгаж, зөв бол JWT token үүсгэнэ.
 */
@Service
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticateUserService(UserRepositoryPort userRepositoryPort,
                                   PasswordEncoder passwordEncoder,
                                   JwtTokenService jwtTokenService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Mono<LoginResponse> login(LoginCommand command) {
        // 1. Email-аар хэрэглэгч хайх, олдохгүй бол 401 алдаа
        return userRepositoryPort.findByEmail(command.email().trim())
                .switchIfEmpty(Mono.error(AuthenticationFailedException.invalidCredentials()))
                .flatMap(user -> validatePassword(user, command.password())
                        .map(validUser -> {
                            JwtTokenService.GeneratedToken token = jwtTokenService.generate(validUser);
                            return new LoginResponse(
                                    token.value(),
                                    "Bearer",
                                    token.expiresInSeconds(),
                                    token.expiresAt(),
                                    validUser.getId(),
                                    validUser.getEmail(),
                                    validUser.getSystemRole(),
                                    validUser.getDepartmentId()
                            );
                        }));
    }

    /**
     * Нууц үг шалгах.
     * Хэрэглэгч идэвхгүй эсэхүл нууц үг таарахгүй бол 401 буцаана.
     */
    private Mono<User> validatePassword(User user, String rawPassword) {
        if (!user.isActive()) {
            return Mono.error(AuthenticationFailedException.invalidCredentials());
        }

        String passwordHash = user.getPasswordHash();
        if (passwordHash == null || !passwordEncoder.matches(rawPassword, passwordHash)) {
            return Mono.error(AuthenticationFailedException.invalidCredentials());
        }

        return Mono.just(user);
    }
}
