package mn.num.edu.user_service.application.dto;

import mn.num.edu.user_service.domain.model.SystemRole;

import java.time.Instant;

/**
 * Нэвтрэлт амжилттай болсон үед буцаах хариу.
 * JWT token, хүчинтэй хугацаа, хэрэглэгчийн мэдээлэл агуулна.
 */
public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        Instant expiresAt,
        String userId,
        String email,
        SystemRole role,
        String departmentId
) {
}
