package org.example.springbootlogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //FixMe: Spring Security 6.1.0 부터는 메서드 체이닝을 지양하고 람다식으로 설정을 지정하도록 변경
    //ex)
    //      formLogin().disable() (X)
    //      formLogin((formLogin) ->
    //          formLogin().disable()
    //      ) (O)

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/", "/login", "/login/oauth").permitAll() //허용 페이지
//                        .anyRequest().authenticated() // 위의 허용 페이지 외에 인증 필요
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .loginPage("/login/oauth") // 커스텀 로그인 페이지
//                                .defaultSuccessUrl("/login/success", true) // 로그인 성공 시 리다이렉트할 URL
//                                .failureUrl("/login/oauth?error=true") // 로그인 실패 시 리다이렉트할 URL
//                )
//                .logout((logout) -> logout.permitAll());
//
//        return http.build();
//    }
    //REST API 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/login", "/pass").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ); // 세션 사용 안 함(토큰 기반 인증 사용할 것)


        return http.build();
    }
}
