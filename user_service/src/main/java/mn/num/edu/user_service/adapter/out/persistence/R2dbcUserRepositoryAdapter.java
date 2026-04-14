package mn.num.edu.user_service.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mn.num.edu.user_service.application.port.out.UserRepositoryPort;
import mn.num.edu.user_service.domain.model.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class R2dbcUserRepositoryAdapter implements UserRepositoryPort {

    private final UserR2dbcRepository repository;

    @Override
    public Mono<User> save(User user) {return repository.save(user);}

    @Override
    public Mono<User> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll();
    }
}
