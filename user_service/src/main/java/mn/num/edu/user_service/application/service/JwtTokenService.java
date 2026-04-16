package mn.num.edu.user_service.application.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import mn.num.edu.user_service.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * JWT token үүсгэх service.
 * HS256 алгоритмаар гарын тоогоор нууц түлхүүрээр token үүсгэнэ.
 * Token дотор userId (subject), email, role, departmentId claim агуулна.
 */
@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;    // JWT encode хийгч
    private final String issuer;              // Token үүсгэгчийн нэр (iss claim)
    private final long expirationMinutes;     // Token хүчинтэй хугацаа (минут)

    public JwtTokenService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.issuer}") String issuer,
            @Value("${app.security.jwt.expiration-minutes}") long expirationMinutes
    ) {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(secretKey));
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    /** Хэрэглэгчийн мэдээллээр JWT token үүсгэх. */
    public GeneratedToken generate(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationMinutes * 60);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getSystemRole().name())
                .claim("departmentId", user.getDepartmentId())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new GeneratedToken(token, expiresAt, expirationMinutes * 60);
    }

    /** Үүсгэгдсэн token-ий мэдээллийг агуулах record. */
    public record GeneratedToken(String value, Instant expiresAt, long expiresInSeconds) {
    }
}
