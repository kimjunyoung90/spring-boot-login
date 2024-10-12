package org.example.springbootlogin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class LoginRestController {

    // 가상 사용자 검증 (예시)
    private boolean validateUser(String username, String password) {
        // 실제로는 DB 조회 등을 통해 사용자를 검증해야 함
        return "admin".equals(username) && "password123".equals(password);
    }

    @PostMapping("/session")
    public ResponseEntity<Void> loginWithSession(@RequestBody HashMap<String, String> param, HttpServletRequest request) {
        String username = param.get("username");
        String password = param.get("password");
        if(validateUser(username, password)) {
            HttpSession session = request.getSession(); //세션 생성
            session.setAttribute("username", username); //세션에 사용자 정보 저장
        }
        return null;
    }
}
