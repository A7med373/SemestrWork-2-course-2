package com.sem.service;

import com.sem.models.user.UserProfile;
import com.sem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    public String generateJwtToken(Authentication authentication, UserProfile userProfile) {
        @Value("${jwt.secret}")
        private String jwtSecret;

        @Value("${jwt.expiration-ms}")
        private long jwtExpirationMs;

        @Value("${jwt.issuer}")
        private String jwtIssuer;

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("userId", userProfile.getId().toString())
                .claim("email", userProfile.getEmail())
                .claim("roles", getRoles(authentication))
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

        // Проверка на администратора
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Проверка владельца ресурса
        String currentUserId = authentication.getName();
        if (currentUserId.equals(resourceOwnerId.toString())) {
            return true;
        }

        // Проверка роли
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
}
