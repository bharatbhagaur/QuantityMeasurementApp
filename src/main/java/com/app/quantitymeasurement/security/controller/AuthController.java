package com.app.quantitymeasurement.security.controller;

import com.app.quantitymeasurement.security.dto.LoginResponse;
import com.app.quantitymeasurement.security.dto.LoginRequest;
import com.app.quantitymeasurement.security.dto.SignupRequest;
import com.app.quantitymeasurement.security.model.UserEntity;
import com.app.quantitymeasurement.security.repository.UserRepository;
import com.app.quantitymeasurement.security.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for login and signup")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TokenService tokenService, 
                          AuthenticationManager authenticationManager, 
                          UserRepository userRepository, 
                          PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Login with username and password and receive a JWT token along with user info")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        String token = tokenService.generateToken(authentication);
        
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication"));
                
        LoginResponse loginResponse = new LoginResponse(
                token, 
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getRoles()
        );
        
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register new user", description = "Sign up using a unique username, email, and password")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRoles("ROLE_USER");

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
}
