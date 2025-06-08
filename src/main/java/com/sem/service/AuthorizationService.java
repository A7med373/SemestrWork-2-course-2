package com.sem.service;

import com.sem.dto.AuthResponse;
import com.sem.dto.UserRegDto;
import com.sem.models.user.Role;
import com.sem.models.user.User;
import com.sem.models.user.UserProfile;
import com.sem.repository.UserProfileRepository;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final UserProfileRepository userProfileRepository;

    public String generateToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean hasAccess(UUID resourceOwnerId, String requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        String currentUserId = authentication.getName();
        if (currentUserId.equals(resourceOwnerId.toString())) {
            return true;
        }

        if (requiredRole != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_" + requiredRole));
        }

        return false;
    }

    private Collection<? extends GrantedAuthority> getRoles(Authentication authentication) {
        return authentication.getAuthorities();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Transactional
    public AuthResponse register(UserRegDto request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(false, "Пользователь с таким email уже существует", null);
        }

        try {
            User savedUser = userService.registerUser(request);
            logger.info("User saved");

            UserProfile profile = new UserProfile();
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            profile.setEmail(request.getEmail());
            profile.setAccount(savedUser);
            profile.setRole(Role.USER);
            userProfileRepository.save(profile);
            logger.info("User profile saved");

            String token = generateToken(savedUser);
            return new AuthResponse(true, "Регистрация успешна", token);
        } catch (IllegalArgumentException e) {
            logger.error("Validation failed", e);
            return new AuthResponse(false, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Registration failed", e);
            return new AuthResponse(false, "Ошибка регистрации. Попробуйте позже", null);
        }
    }

    public AuthResponse authenticate(UserRegDto request) {
        logger.info("Authentication attempt for email: {}", request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            logger.info("User authenticated: {}", user.getEmail());

            String token = generateToken(user);
            return new AuthResponse(true, "Вход выполнен успешно", token);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for email: {}", request.getEmail(), e);
            return new AuthResponse(false, "Неверный email или пароль", null);
        } catch (Exception e) {
            logger.error("Unexpected authentication error", e);
            return new AuthResponse(false, "Ошибка сервера при аутентификации", null);
        }
    }
}
