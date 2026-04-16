package mn.num.edu.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Gateway-н аюулгүй байдлын тохиргоо.
 * JWT token шалгах, role-д суурилсан хандалтын хяналт хийнэ.
 * user_service-ээс үүссэн HS256 JWT token-г decode хийж, role claim-аас эрхийг тодорхойлно.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * HTTP аюулгүй байдлын шүүлтүүрийн гинж (filter chain) тодорхойлно.
     * Endpoint бүрт ямар role шаардагдахыг зааж өгнө.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // REST API учир CSRF хамгаалалт шаардлагагүй
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS preflight хүсэлтийг нээлттэй
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll() // Health check нээлттэй
                        .pathMatchers("/api/auth/**").permitAll() // Нэвтрэх endpoint нээлттэй
                        .pathMatchers(HttpMethod.POST,
                                "/api/users/students",
                                "/api/users/teachers",
                                "/api/users/departments"
                        ).hasRole("ADMIN") // Хэрэглэгч үүсгэх зөвхөн ADMIN
                        .pathMatchers(HttpMethod.GET, "/api/users/all").hasRole("DEPARTMENT") // Бүх хэрэглэгч харах - DEPARTMENT
                        .pathMatchers("/api/workflows/**").hasRole("DEPARTMENT") // Ажлын урсгал - DEPARTMENT
                        .pathMatchers("/api/evaluations/**").hasAnyRole("TEACHER", "DEPARTMENT") // Үнэлгээ - TEACHER, DEPARTMENT
                        .pathMatchers("/api/grades/**", "/api/grading/**", "/api/resolutions/**").hasRole("DEPARTMENT") // Дүн, тогтоол - DEPARTMENT
                        .pathMatchers("/api/reports/**").hasAnyRole("TEACHER", "DEPARTMENT") // Тайлан - TEACHER, DEPARTMENT
                        .anyExchange().authenticated() // Бусад бүх endpoint нэвтэрсэн байх шаардлагатай
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    /**
     * JWT token-г decode хийх bean.
     * application.yml дахь нууц түлхүүрээр HS256 алгоритмаар баталгаажуулна.
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${app.security.jwt.secret}") String secret) {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /** JWT token-г Spring Security authentication объект руу хөрвүүлэгч. */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter::convert;
    }

    /**
     * JWT claim-с role-г уншиж, Spring Security-н GrantedAuthority руу хөрвүүлнэ.
     * Жишээ: claim "role": "STUDENT" -> authority "ROLE_STUDENT"
     */
    private Flux<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String role = jwt.getClaimAsString("role");

        if (role != null && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return Flux.fromIterable(authorities);
    }
}
