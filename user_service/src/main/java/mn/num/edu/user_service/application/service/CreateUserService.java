package mn.num.edu.user_service.application.service;


import mn.num.edu.user_service.application.dto.CreateUserCommand;
import mn.num.edu.user_service.application.port.in.CreateUserUseCase;
import mn.num.edu.user_service.application.port.out.UserEventPublisherPort;
import mn.num.edu.user_service.application.port.out.UserRepositoryPort;
import mn.num.edu.user_service.domain.event.UserCreatedEvent;
import mn.num.edu.user_service.domain.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserEventPublisherPort userEventPublisherPort;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(
            UserRepositoryPort userRepositoryPort,
            @Qualifier("kafkaUserEventPublisherAdapter") UserEventPublisherPort userEventPublisherPort,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.userEventPublisherPort = userEventPublisherPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> execute(CreateUserCommand command) {
        User user = User.create(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.systemRole(),
                command.departmentId(),
                passwordEncoder.encode(command.password())
        );

        return userRepositoryPort.save(user)
                .flatMap(saved ->
                        userEventPublisherPort.publishUserCreated(UserCreatedEvent.from(saved))
                                .thenReturn(saved)
                );
    }
}
