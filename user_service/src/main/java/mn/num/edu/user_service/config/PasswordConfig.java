package mn.num.edu.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Нууц үг хэшлэх тохиргоо.
 * BCrypt алгоритм ашиглан нууц үгийг хэшлэж хадгална.
 */
@Configuration
public class PasswordConfig {

    /** BCrypt нууц үг хэшлэгч bean. Бүх service-д ашиглагдана. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
