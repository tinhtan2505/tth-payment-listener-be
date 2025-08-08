package tth_group.payment_listener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(resolveCurrentUsername()).filter(s -> !s.isBlank());
    }

    private static String resolveCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "system";

        Object principal = auth.getPrincipal();

        // Java 21+ pattern matching for switch (ổn trên JDK 23)
        return switch (principal) {
            case UserDetails ud -> nonBlankOr(ud.getUsername(), auth.getName());
            case Jwt jwt -> nonBlankOr(jwt.getClaimAsString("preferred_username"),
                    nonBlankOr(jwt.getClaimAsString("email"),
                            nonBlankOr(jwt.getSubject(), auth.getName())));
            case String s -> nonBlankOr(s, auth.getName());
            default -> nonBlankOr(auth.getName(), "system");
        };
    }

    private static String nonBlankOr(String v, String fallback) {
        return (v != null && !v.isBlank()) ? v : fallback;
    }
}