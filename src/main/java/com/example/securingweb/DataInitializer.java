package com.example.securingweb;

import com.example.securingweb.model.Role;
import com.example.securingweb.model.User;
import com.example.securingweb.repository.RoleRepository;
import com.example.securingweb.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            Role userRole = roleRepo.findByName("USER").orElseGet(() -> roleRepo.save(new Role() {{ setName("USER"); }}));
            Role adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> roleRepo.save(new Role() {{ setName("ADMIN"); }}));
            if (userRepo.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(encoder.encode("password"));
                user.setRoles(Set.of(userRole));
                userRepo.save(user);
            }
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));
                userRepo.save(admin);
            }
        };
    }
}
