package com.cosmomedia.location.authentication;

import com.cosmomedia.location.config.JwtService;
import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.enums.Roles;
import com.cosmomedia.location.repositories.BalanceRepository;
import com.cosmomedia.location.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BalanceRepository balanceRepository;

    public AuthenticationResponse register(RegisterRequest request) {

        var user = Users.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.CUSTOMER)
                .email(request.getEmail())
                .createdAt(new Date())
//                .picture(request.getPicture())
                .build();
        repository.save(user);
        Balance initialBalance = Balance.builder()
                .amount(0.0)
                .users(user)
                .build();
        balanceRepository.save(initialBalance);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    private UsersDto convertToUsersDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        usersDto.setEmail(users.getEmail());
//        if (users.getPicture() != null) {
//            String pictureString = new String(users.getPicture(), StandardCharsets.UTF_8);
//            usersDto.setPicture(pictureString);
//        }
        usersDto.setRole(users.getRole());
        usersDto.setCreatedAt(users.getCreatedAt());
        return usersDto;
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(convertToUsersDto(user))
                .build();
    }
}