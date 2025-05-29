package com.example.userservice.service;

import com.example.userservice.entity.UserInfo;
import com.example.userservice.repository.UserInfoRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public static final String SECRET ="1231231312313131313131313131313131313131313123424242424242342242";

    public String generateToken(String userName) {
        Map<String,Object> claims = new HashMap<>();
        long currentTime = System.currentTimeMillis();

        Optional<UserInfo> userOpt = userInfoRepository.findByName(userName);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserInfo user = userOpt.get();

        claims.put("id", user.getId());
        claims.put("username", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 1000 * 60 * 5))
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