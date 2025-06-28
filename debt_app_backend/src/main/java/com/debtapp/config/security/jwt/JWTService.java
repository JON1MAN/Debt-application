package com.debtapp.config.security.jwt;

import com.debtapp.config.security.SecurityUser;
import com.debtapp.config.security.SecurityUserService;
import com.debtapp.user.controller.auth.mapper.dto.AuthToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;
    private final long accessTokenExpiration = 1000L * 60 * 30;
    private static final Logger log = LogManager.getLogger(JWTService.class);
    private final SecurityUserService securityUserService;

    public String generateToken(UserDetails userDetails, boolean isRefresh) {
        return generateToken(new HashMap<>(), userDetails, isRefresh);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails, boolean isRefresh) {

        if (userDetails instanceof SecurityUser) {
            claims.put("id", ((SecurityUser) userDetails).getUser().getId());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public AuthToken generateAuthTokens(SecurityUser securityUser) {
        String accessToken = generateToken(securityUser, false);
        return AuthToken.builder()
                .access_token(accessToken)
                .username(securityUser.getUsername())
                .build();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, null);
    }


}
