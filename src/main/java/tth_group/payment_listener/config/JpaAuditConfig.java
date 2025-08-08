package tth_group.payment_listener.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import tth_group.payment_listener.entity.User;
import tth_group.payment_listener.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * AuditorAware<UUID>:
 * - Lấy UUID từ JWT (sub/user_id/uid) nếu có.
 * - Nếu không, lấy username (preferred_username/email hoặc principal.getName()) rồi tra DB -> UUID.
 * - Nếu chưa đăng nhập, gán SYSTEM_UUID (bạn có thể đổi thành Optional.empty() nếu muốn để null).
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
public class JpaAuditConfig {

    // TODO: thay thế bằng repository của bạn
    private final UserRepository userRepository;

    // UUID mặc định cho các thao tác hệ thống/không xác định
    private static final UUID SYSTEM_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> resolveCurrentUserId().or(() -> Optional.of(SYSTEM_UUID));
        // Nếu muốn để trống khi không xác định, dùng: return this::resolveCurrentUserId;
    }

    private Optional<UUID> resolveCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        // 1) JWT: ưu tiên lấy UUID trực tiếp từ claim
        if (principal instanceof Jwt jwt) {
            // Thử các claim phổ biến cho user-id
            String idClaim = firstNonBlank(
                    jwt.getClaimAsString("sub"),
                    jwt.getClaimAsString("user_id"),
                    jwt.getClaimAsString("uid")
            );
            Optional<UUID> parsed = parseUuid(idClaim);
            if (parsed.isPresent()) return parsed;

            // Không có UUID? Lấy username/email rồi tra DB sang UUID
            String username = firstNonBlank(
                    jwt.getClaimAsString("preferred_username"),
                    jwt.getClaimAsString("email"),
                    jwt.getClaimAsString("name")
            );
            if (isNotBlank(username)) {
                return findUserIdByUsername(username);
            }
            // Fallback cuối cùng: auth.getName()
            if (isNotBlank(auth.getName())) {
                return findUserIdByUsername(auth.getName());
            }
            return Optional.empty();
        }

        // 2) UserDetails: lấy username rồi tra DB
        if (principal instanceof UserDetails ud) {
            String username = ud.getUsername();
            if (isNotBlank(username)) {
                return findUserIdByUsername(username);
            }
            // Fallback
            return findUserIdByUsername(auth.getName());
        }

        // 3) String principal: có thể đã chứa UUID, nếu không thì coi như username
        if (principal instanceof String s) {
            Optional<UUID> parsed = parseUuid(s);
            if (parsed.isPresent()) return parsed;
            if (isNotBlank(s)) return findUserIdByUsername(s);
        }

        // 4) Fallback: thử auth.getName() là UUID hoặc username
        Optional<UUID> parsed = parseUuid(auth.getName());
        if (parsed.isPresent()) return parsed;
        if (isNotBlank(auth.getName())) return findUserIdByUsername(auth.getName());

        return Optional.empty();
    }

    private Optional<UUID> findUserIdByUsername(String username) {
        // Implement theo domain của bạn:
        // Ví dụ: userRepository.findByUsername(String) trả về entity có getId(): UUID
        return userRepository.findByUsername(username).map(User::getId);
    }

    private static Optional<UUID> parseUuid(String value) {
        if (!isNotBlank(value)) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(value.trim()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static String firstNonBlank(String... candidates) {
        for (String c : candidates) if (isNotBlank(c)) return c;
        return null;
    }
}
