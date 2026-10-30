package com.odc.digilearn.modules.user.repository;

import com.odc.digilearn.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByActivationTokenHash(String activationTokenHash);
    boolean existsByEmailIgnoreCase(String email);
}
