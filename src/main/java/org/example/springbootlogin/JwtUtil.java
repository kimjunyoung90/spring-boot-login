package org.example.springbootlogin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // HS256에 안전한 SecretKey 생성 (자동으로 256비트 이상 생성)
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String usernamme) {
        return Jwts.builder()
                .setSubject(usernamme) // 토큰의 주체로 사용자 이름 설정
                .setIssuedAt(new Date()) // 토큰 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 만료 시간 (10시간)
                .signWith(SECRET_KEY) // 서명 알고리즘 및 비밀키 설정
                .compact();
    }
}
