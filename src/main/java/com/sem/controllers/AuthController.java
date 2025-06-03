package com.sem.controllers;

import com.sem.service.CustomAuthenticationManager;
import com.sem.dto.JwtResponse;
import com.sem.dto.UserRegDto;
import com.sem.jwt.JwtTokenProvider;
import com.sem.models.user.User;
import com.sem.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final CustomAuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserProfileService userProfileService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRegDto userRegDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRegDto.getEmail(),
                        userRegDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtTokenProvider.createToken(authentication, userProfileService.getUserProfileByEmail(userRegDto.getEmail()).orElseThrow());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles
        ));
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegDto()); // Пустой DTO для формы
        return "register"; // Возвращает register.html (Thymeleaf/Freemarker)
    }
}
