package com.example.Auth.controller;

import com.example.Auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/reissue")
    @Operation(description = "Access, Refresh 토큰 재발급")
    public ResponseEntity<?> test(HttpServletRequest request, HttpServletResponse response) {
        HttpServletResponse res = tokenService.reissueRefreshToken(request, response);
        return ResponseEntity.status(res.getStatus()).build();
    }

}
