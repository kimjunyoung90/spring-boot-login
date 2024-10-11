package org.example.springbootlogin.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.springbootlogin.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    // 가상 사용자 검증 (예시)
    private boolean validateUser(String username, String password) {
        // 실제로는 DB 조회 등을 통해 사용자를 검증해야 함
        return "admin".equals(username) && "password123".equals(password);
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @GetMapping("/oauth")
    public String loginOAuth() {
        return "login-oauth";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "login-success";
    }

    @PostMapping("/cookie")
    public String loginWithCookie(@RequestBody HashMap<String, String> param, HttpServletResponse response) {
        String username = param.get("username");
        String password = param.get("password");
        if(validateUser(username, password)) {
            Cookie cookie = new Cookie("username", username);
            cookie.setHttpOnly(true); // JavaScript로 접근 금지
            cookie.setMaxAge(60 * 60); // 쿠키 유효시간 1시간
            cookie.setPath("/"); // 모든 경로에서 접근 가능
            response.addCookie(cookie);
            return "redirect:/login/success";
        }
        return "login";
    }

    @PostMapping("/session")
    public String loginWithSession(@RequestBody HashMap<String, String> param, HttpServletRequest request) {
        String username = param.get("username");
        String password = param.get("password");
        if(validateUser(username, password)) {
            HttpSession session = request.getSession(); //세션 생성
            session.setAttribute("username", username); //세션에 사용자 정보 저장
            return "redirect:/login/success";
        }
        return "login";
    }

    @PostMapping("/jwt")
    public String loginWithJWT(@RequestBody HashMap<String, String> param, HttpServletResponse response) {
        String username = param.get("username");
        String password = param.get("password");
        if(validateUser(username, password)) {
            String JWT = jwtUtil.generateToken(username);
            Cookie cookie = new Cookie("token", JWT);
            response.addCookie(cookie);
            return "redirect:/login/success";
        }
        return "login";
    }
}
