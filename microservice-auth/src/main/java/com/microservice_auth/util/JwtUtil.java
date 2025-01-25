package com.microservice_auth.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.microservice_auth.constants.ErrorMessageConstants.*;
import static com.microservice_auth.constants.GeneralConstants.EMAIL_TOKEN_EXPIRATION_HOURS;

@Service
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key SECRET_KEY;

    @PostConstruct
    public void init() {
        this.SECRET_KEY = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    //private static final long LOGIN_TOKEN_VALIDATION_TIME = 20_000; // 20 segundos

    private static final long LOGIN_TOKEN_VALIDATION_TIME = 1000 * 60 * 60 * 24 * 14;
    private static final long EMAIL_TOKEN_EXPIRATION_IN_MILLISECONDS = 1000 * 60 * 60 * EMAIL_TOKEN_EXPIRATION_HOURS;


    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae la fecha de expiración del token JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrae cualquier "claim" (información) del token JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException(TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            throw new JwtException(MALFORMED_TOKEN);
        } catch (SignatureException e) {
            throw new JwtException(INVALID_TOKEN_SIGNATURE);
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    // Verifica si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Genera un token JWT para el usuario
    // Generar un token JWT con idUser, username, y roles
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, userId, LOGIN_TOKEN_VALIDATION_TIME);
    }

    private String createToken(Map<String, Object> claims, String subject, long validity) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Genera un token JWT para la verificación de email
    public String generateEmailVerificationToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, EMAIL_TOKEN_EXPIRATION_IN_MILLISECONDS);
    }

    public Boolean validateToken(String token) {
        extractAllClaims(token);
        // Verifica si el token ha expirado
        return !isTokenExpired(token);
    }






}
