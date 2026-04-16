package mn.num.edu.user_service.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Нэвтрэх хүсэлтийн формат. Email болон нууц үг заавал шаардлагатай. */
public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
