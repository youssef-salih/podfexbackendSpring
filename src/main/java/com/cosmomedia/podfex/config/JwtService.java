package com.cosmomedia.podfex.config;


import com.cosmomedia.podfex.entities.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "3ff7fc1eed5d1a4c4bbebf033af39964f7d9144c90b94354a2814f3647542e93";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public<T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Include single role in extra claims as a string
        String role = userDetails.getAuthorities().stream()
                .map(e -> e.getAuthority())
                .findFirst()
                .orElse(null);

        extraClaims.put("role", role);
        if (userDetails instanceof Users) {
            Users user = (Users) userDetails;
            extraClaims.put("username", user.getFullName());
        }
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        long expirationMillis;
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CUSTOMER"))) {
            // User is a customer, set expiration to one month (30 days)
            expirationMillis = 1000L * 60 * 60 * 24 * 30;
        } else {
            // User is an admin, set expiration to 24 hours
            expirationMillis = 1000L * 60 * 60 * 24;
        }
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder().
                setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
