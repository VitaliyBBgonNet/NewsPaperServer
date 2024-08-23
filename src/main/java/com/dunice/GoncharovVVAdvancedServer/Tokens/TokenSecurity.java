package com.dunice.GoncharovVVAdvancedServer.Tokens;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class TokenSecurity {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generationTokenFroRegistration(String userId, String userName, String role) {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim(userName, role)
                .signWith(key)
                .compact();
    }
}
