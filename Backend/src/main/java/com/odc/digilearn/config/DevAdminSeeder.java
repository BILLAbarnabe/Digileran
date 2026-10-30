package com.odc.digilearn.config;

import com.odc.digilearn.modules.user.domain.Role;
import com.odc.digilearn.modules.user.domain.User;
import com.odc.digilearn.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;

@Configuration
@Profile("dev")
public class DevAdminSeeder {
    @Bean
    CommandLineRunner seedDevAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${digilearn.dev-admin.email:admin@digilearn.local}") String email,
            @Value("${digilearn.dev-admin.password:AdminTest123456}") String password) {
        return args -> {
            if (!userRepository.existsByEmailIgnoreCase(email)) {
                User admin = new User(email, passwordEncoder.encode(password), EnumSet.of(Role.ADMIN));
                admin.activate();
                userRepository.save(admin);
            }
        };
    }
}