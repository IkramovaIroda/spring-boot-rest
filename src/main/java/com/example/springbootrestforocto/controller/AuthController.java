package com.example.springbootrestforocto.controller;

import com.example.springbootrestforocto.dto.LoginDTO;
import com.example.springbootrestforocto.security.JwtProvider;
import com.example.springbootrestforocto.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    final JwtProvider jwtProvider;
    final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {

        UserDetails userDetails = authService.loadUserByUsername(loginDTO.getName());
        String token = jwtProvider.generateToken(loginDTO.getName());
        return ResponseEntity.ok().body(token);
    }
}
