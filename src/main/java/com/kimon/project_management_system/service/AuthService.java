package com.kimon.project_management_system.service;

import com.kimon.project_management_system.model.User;
import com.kimon.project_management_system.repo.UserRepo;
import com.kimon.project_management_system.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthenticationManager authManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<AuthResponse> register(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());
        if(existingUser != null){
            return new ResponseEntity<>(new AuthResponse(null, "Email already in use", null), HttpStatus.BAD_REQUEST);
        }

        try {
            user.setPassword(encoder.encode(user.getPassword()));
            User newUser = userRepo.save(user);
            return new ResponseEntity<>(new AuthResponse(newUser, "Registration successful", null), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(null, "Registration failed: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AuthResponse> login(User user){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(auth.isAuthenticated()){
            String jwt = jwtService.generateToken(user.getEmail());
            return new ResponseEntity<>(new AuthResponse(user, "Login successfully", jwt), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new AuthResponse(), HttpStatus.BAD_REQUEST);
    }

}
