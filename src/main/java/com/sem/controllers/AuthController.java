package com.sem.controllers;

import com.sem.dto.AuthResponse;
import com.sem.dto.UserRegDto;
import com.sem.service.AuthorizationService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthorizationService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @ResponseBody
    @PostMapping("/api/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRegDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @ResponseBody
    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRegDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/api/auth/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/";
    }
}
