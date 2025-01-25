package com.microservice.gateway.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static com.microservice.gateway.constants.ErrorMessageConstants.*;


@Service
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;


    private final Key SECRET_KEY = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }


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
    public Boolean isTokenValid(String token) {
        extractAllClaims(token);
        // Verifica si el token ha expirado
        return !isTokenExpired(token);
    }


}

