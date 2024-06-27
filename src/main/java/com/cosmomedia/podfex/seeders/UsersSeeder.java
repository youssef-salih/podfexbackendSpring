package com.cosmomedia.podfex.seeders;


import com.cosmomedia.podfex.entities.Users;
import com.cosmomedia.podfex.enums.Roles;
import com.cosmomedia.podfex.enums.Status;
import com.cosmomedia.podfex.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UsersSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    public void seedData() {
        createDefaultUser("youssefsalih2016@gmail.com", "machakil1", "youssef", "salih", Roles.ADMIN);
    }

    private void createDefaultUser(String email, String password, String firstName, String lastName, Roles role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            Users user = Users.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .firstName(firstName)
                    .lastName(lastName)
                    .createdAt(new Date())
                    .status(Status.ACTIVE)
                    .role(role)
                    .build();

            userRepository.save(user);
        }
    }
}