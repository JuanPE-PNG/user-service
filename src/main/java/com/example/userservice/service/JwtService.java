package com.example.userservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    public static final String SECRET ="1231231312313131313131313131313131313131313123424242424242342242";

    public String generateToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime+1000*60*5))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();


    }

    public void validateToken(String token){
        Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}