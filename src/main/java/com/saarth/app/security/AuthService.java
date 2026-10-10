package com.saarth.app.security;

import com.saarth.app.dto.LoginRequest;
import com.saarth.app.dto.LoginResponse;
import com.saarth.app.dto.SignupRequest;
import com.saarth.app.dto.SignupResponse;
import com.saarth.app.persistance.entity.User;
import com.saarth.app.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return LoginResponse.builder()
                .jwt(token)
                .userId(user.getUserId()).build();
    }

    public SignupResponse signup(SignupRequest signupRequest) {
        User user = userRepository.findByEmail(signupRequest.getUsername()).orElse(null);
        if (user != null) { throw  new IllegalArgumentException("User already exists"); }
        Instant now = Instant.now();
        user = User.builder().name(signupRequest.getFirstName() + " " + signupRequest.getLastName())
                .email(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .createdAt(now)
                .updatedAt(now)
                .build();
        user = userRepository.save(user);
        return SignupResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .build();
    }
}
