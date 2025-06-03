package com.sem.jwt;

import com.sem.models.user.UserProfile;
import com.sem.service.AuthorizationService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final SecretKey secret; // Замените на реальный ключ
    private final long validityInMilliseconds; // 1 час
    private final AuthorizationService authorisationService;
    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") long validity,
                            AuthorizationService authorisationService){
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        validityInMilliseconds = validity;
        this.authorisationService = authorisationService;
    }


    public String createToken(Authentication auth, UserProfile user) {
        // Используем существующий сервис для генерации
        return authorisationService.generateJwtToken(auth, user);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) throws Exception {
        try {
            return !parseToken(token).getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new Exception("JWT token is expired or invalid");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseToken(token);
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();  // Получаем Claims напрямую
    }
}
