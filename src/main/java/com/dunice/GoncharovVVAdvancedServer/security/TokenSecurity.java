package com.dunice.GoncharovVVAdvancedServer.security;

import com.dunice.GoncharovVVAdvancedServer.constants.StringConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class TokenSecurity {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    public String generateToken(UUID id) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());

        return StringConstants.BEARER + Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

    }

    public Boolean isTokenValid(String token) {
        try {
            Claims claims = getClaimsToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getIdFromToken(String token) {
        Claims claims = getClaimsToken(token);
        return claims.getSubject();
    }

    private Claims getClaimsToken(String token) {
        byte[] keyByte = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyByte, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
