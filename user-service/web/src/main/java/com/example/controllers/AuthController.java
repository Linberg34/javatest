package com.example.controllers;

import com.application.service.interfaces.AuthService;
import com.application.util.TokenPair;
import com.example.dtos.RefreshTokenRequest;
import com.example.dtos.UserLoginRequest;
import com.example.responses.AuthResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private  final AuthService authService;

    public  AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public TokenPair login(@RequestBody UserLoginRequest loginRequest){
        return  authService.login(loginRequest.getEmail(),loginRequest.getPassword());
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request){
        String newAccessToken = authService.refreshToken(request.getRefreshToken());
        return  new AuthResponse(newAccessToken,request.getRefreshToken());
    }
}
