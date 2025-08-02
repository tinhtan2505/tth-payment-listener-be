package tth_group.payment_listener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tth_group.payment_listener.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

