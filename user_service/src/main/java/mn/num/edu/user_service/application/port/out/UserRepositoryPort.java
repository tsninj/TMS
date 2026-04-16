package mn.num.edu.user_service.application.port.out;

import mn.num.edu.user_service.domain.model.User;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@NoRepositoryBean

public interface UserRepositoryPort {
    Mono<User> save(User user);
    Mono<User> findById(String id);
    Mono<User> findByEmail(String email);
    Flux<User> findAll();
}
