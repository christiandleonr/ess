package com.easysplit.ess.iam.application;

import com.easysplit.ess.iam.domain.contracts.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${spring.security.jwt.secret}")
    private String secret;
    public static final long EXPIRATION_TIME = 1000 * 60 * 5; // 5 minutes
    public static final long RT_EXPIRATION_TIME = 1000 * 60 * 1; // 10 minutes

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateToken(String email, boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();

        long expirationTime = isRefreshToken ? RT_EXPIRATION_TIME : EXPIRATION_TIME;
        return createToken(claims, email, expirationTime);
    }

    /**
     * Creates a token adding claims and the email as subject
     *
     * @param claims claims to be added
     * @param email email used as subject
     * @return created token
     */
    private String createToken(Map<String, Object> claims, String email, long expirationTime) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Extracts all the claims from a given token
     * @param token token from which we extract all the claims
     *
     * @return claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates if a given token has expired
     *
     * @param token token to be validated
     * @return true if the token is expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates the sign key from our secret
     *
     * @return sign key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
