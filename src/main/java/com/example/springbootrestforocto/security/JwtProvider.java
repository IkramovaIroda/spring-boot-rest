package com.example.springbootrestforocto.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    public String generateToken(String username) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, "KETMONJWT")
                .setIssuedAt(new Date()) //xozir berildi
                .setExpiration(new Date(System.currentTimeMillis() + 86400 * 1000)) //1 kun amal qiladi
                .setSubject(username)
                .compact();
    }

    //tokendan kimligini aniqlash
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey("KETMONJWT")
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); //username
    }

}
