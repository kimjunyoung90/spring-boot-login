package org.example.springbootlogin.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootlogin.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginRestController {

    @Autowired
    private JwtUtil jwtUtil;

    private Map<String, Map<String, Object>> users;

    public LoginRestController() {
        users = new HashMap<>();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", "admin");
        user1.put("name", "junyoung kim");
        user1.put("email", "wnsdud1900427@gmail.com");
        user1.put("avatar", "https://cdn-icons-png.freepik.com/512/4661/4661320.png");
        user1.put("bio", "안녕하세요. 풀스택 개발자 김준영입니다.");
        users.put("admin", user1);
    }

    private Map<String, Object> getUsers(String id) {
        return users.get(id);
    }

    // 가상 사용자 검증 (예시)
    private boolean validateUser(String id, String password) {
        // 실제로는 DB 조회 등을 통해 사용자를 검증해야 함
        return "admin".equals(id) && "password123".equals(password);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody HashMap<String, String> param, HttpServletResponse response) {
        String id = param.get("id");
        String password = param.get("password");
        if(validateUser(id, password)) {

            //JWT 생성
            String JWT = jwtUtil.generateToken(id);

            Cookie cookie = new Cookie("JWT", JWT);
            response.addCookie(cookie);

            // 응답으로 반환할 Map
            Map<String, Object> result = new HashMap<>();
            result.put("resultCode", 200);
            result.put("message", "Login successful");

            // ResponseEntity로 반환
            return ResponseEntity.ok(result);
        }
        // 로그인 실패 시 응답
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("resultCode", 401);
        errorResponse.put("message", "Invalid credentials");

        return ResponseEntity.status(401).body(errorResponse);
    }

    @GetMapping("/oauth2/login/success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        return "Welcome, " + email;
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String id) {
        return ResponseEntity.ok(getUsers(id));
    }
}
