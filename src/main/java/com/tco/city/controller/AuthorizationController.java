package com.tco.city.controller;

import com.tco.city.dto.AuthResponse;
import com.tco.city.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

  private UserDetailsService userDetailsService;
  private PasswordEncoder passwordEncoder;

  AuthorizationController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
    if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword()))
      return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    return ResponseEntity.ok(
        AuthResponse.builder()
            .name(loginRequest.getUsername())
            .roles(userDetails.getAuthorities().stream().map(role -> role.toString()).collect(Collectors.toList()))
            .build());
  }
}
