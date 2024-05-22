package com.example.springsecuritysecondhw.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    private static String JWT_SECRET_KEY;

    @Value("${token.secret}")
    public void setJwtSecretKey(String JWT_SECRET_KEY) {
        JwtUtil.JWT_SECRET_KEY = JWT_SECRET_KEY;
    }


    private static long EXPIRATION_TIME;

    @Value("${token.timeLife}")
    public void setExpirationTime(Long EXPIRATION_TIME) {
        JwtUtil.EXPIRATION_TIME = EXPIRATION_TIME;
    }

    public static String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);
        return createToken(claims, userDetails.getUsername());
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        return extractAllClaimsForToken(token).getSubject();
    }

    public static List<String> extractRoles(String token) {
        return extractAllClaimsForToken(token).get("roles", List.class);
    }

    public static Date extractExpiration(String token) {
        return extractAllClaimsForToken(token).getExpiration();
    }

    private static Claims extractAllClaimsForToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Время жизни токена истекло");
        } catch (SignatureException e) {
            log.info("Некоррекнтая подпись токена");
        }
        return null;
    }


        private static boolean isTokenExpired (String token){
            return extractExpiration(token).before(new Date());
        }


    }
