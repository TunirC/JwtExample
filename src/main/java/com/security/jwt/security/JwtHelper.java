package com.security.jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    public static final long JWT_TOKEN_VALIDITY = 15 * 60 * 1000; // min * sec * ms
    private String secret = "J3Chav3kh4Wbni+xQVDouuU+i1qa/TeoWMckqtg3+Rgsajwj7WCkXUke9kIuXUojEan5yaVxqAR0G/WkZt6X9vI0vyluTMbERcJULhA+JtsOgCTJ+mHyvTtCypn9Ojeavw==";

    // Get All Claims From Token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    // Get Specific Claim from token
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSecretKey() {
        byte[] bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claimsMap = new HashMap<>();
        return buildToken(claimsMap, userDetails.getUsername());
    }

    private String buildToken(Map<String, Object> claimsMap, String subject) {
        return Jwts.builder().claims(claimsMap).subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String user = getUserNameFromToken(token);
        return !isTokenExpired(token) && user.equals(userDetails.getUsername());
    }
}
