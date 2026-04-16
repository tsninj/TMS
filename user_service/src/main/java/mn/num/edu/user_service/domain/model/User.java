package mn.num.edu.user_service.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Хэрэглэгчийн домэйн модел.
 * DB-ийн users хүснэгтэй холбогдоно.
 * Persistable интерфэйс хэрэгжүүлснээр R2DBC insert/update ялгаа хийнэ.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User implements Persistable<String> {

    @Id
    private String id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    private String email;
    private String departmentId;
    @Column("system_role")
    private SystemRole systemRole;

    @JsonIgnore // API response-д нууц үгийн hash гарахгүй
    @Column("password_hash")
    private String passwordHash;

    private boolean active;

    @Column("created_at")
    private Instant createdAt;

    @Transient // DB-д хадгалагдахгүй, зөвхөн R2DBC insert/update ялгахад ашиглагдана
    private boolean isNew;

    /** Нууц үггүй хэрэглэгч үүсгэх (legacy). */
    public static User create(String firstName, String lastName, String email, SystemRole systemRole, String departmentId) {
        return create(firstName, lastName, email, systemRole, departmentId, null);
    }

    /** Нууц үгтэй хэрэглэгч үүсгэх. UUID автоматаар үүснэ. */
    public static User create(String firstName, String lastName, String email, SystemRole systemRole, String departmentId, String passwordHash) {
        return new User(
                UUID.randomUUID().toString(),
                firstName,
                lastName,
                email,
                departmentId,
                systemRole,
                passwordHash,
                true,
                Instant.now(),
                true
        );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
