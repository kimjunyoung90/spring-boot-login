package org.example.springbootlogin.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootlogin.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); //jwt 토큰 추출
        }

        if(token != null && jwtUtil.isTokenValid(token)) {
            //JWT 가 유효하다면 SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(token));
        }

        // 필터 체인의 다음 필터로 넘어감
        filterChain.doFilter(request, response);
    }
}
