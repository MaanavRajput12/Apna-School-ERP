package com.example.collegedb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.collegedb.Repository.AdminRepository;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.entity.Admin;
import com.example.collegedb.entity.Users;

@Configuration
public class AdminBootstrapConfig {
    private static final String ADMIN_EMAIL = "tcetmunmumbai@gmail.com";
    private static final String ADMIN_PASSWORD = "Blackbadger@12";

    @Bean
    CommandLineRunner bootstrapAdmin(
        UsersRepository usersRepository,
        AdminRepository adminRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Users user = usersRepository.findByUsername(ADMIN_EMAIL).orElseGet(() -> {
                Users created = new Users();
                created.setUsername(ADMIN_EMAIL);
                created.setRole("ADMIN");
                created.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                return usersRepository.save(created);
            });

            boolean userChanged = false;
            user.setUsername(ADMIN_EMAIL);
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                user.setRole("ADMIN");
                userChanged = true;
            }
            if (!passwordEncoder.matches(ADMIN_PASSWORD, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                userChanged = true;
            }
            if (userChanged) {
                user = usersRepository.save(user);
            }

            if (adminRepository.findByUserUserId(user.getUserId()).isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername(ADMIN_EMAIL);
                admin.setPassword(ADMIN_PASSWORD);
                admin.setDob("");
                admin.setUser(user);
                adminRepository.save(admin);
            }
        };
    }
}
