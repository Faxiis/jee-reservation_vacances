package fr.tpreservation.config;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final String KEY = "19D6IjLAudjoZMxFHnp/Owq2SKapi7JRqGhUo82TrAMF9JBz7ATG4SnDLulvQqI2";
    // private static final String KEY = RandomStringUtils.randomAlphanumeric(64);
    private JwtUtil() { }
    public static String generate(String username, List<String> roles) {
        // Création de la clé de signature
        SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
        Date now = new Date();
        return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + 36_000_000))
        .claim("roles", roles)
        .signWith(key)
        .compact();
    }

    public static boolean isValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
            
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            System.out.println(claims.get("info1", String.class));
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static String getSubjectFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());

        Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

        
        return claims.getSubject();
    }

    public static List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
    
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    
        List<String> roles = claims.get("roles", List.class);
    
        return roles.stream()
            .map(role -> "ROLE_" + role.toUpperCase())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
