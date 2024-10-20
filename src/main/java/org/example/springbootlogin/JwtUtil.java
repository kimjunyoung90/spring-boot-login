package org.example.springbootlogin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    // HS256에 안전한 SecretKey 생성 (자동으로 256비트 이상 생성)
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String id) {
        return Jwts.builder()
                .setSubject(id) // 토큰의 주체로 사용자 이름 설정
                .setIssuedAt(new Date()) // 토큰 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 만료 시간 (10시간)
                .signWith(SECRET_KEY) // 서명 알고리즘 및 비밀키 설정
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public Authentication getAuthentication(String token) {
        // JWT를 파싱하여 사용자 정보를 가져옵니다.
        String userId = extractUserId(token);
        List<GrantedAuthority> authorities = Collections.emptyList();

        UserDetails userDetails = new User(userId, "", authorities); // UserDetails 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities); // 인증 객체 생성
    }
}
