package com.StarJ.food_recipe.Securities;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/login", "/h2-console", "/user/signup").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager").hasAnyRole(UserRole.ADMIN.name(), UserRole.MANAGER.name())
                        .requestMatchers("/admin").hasRole(UserRole.ADMIN.name())
                        .anyRequest().permitAll()
                )
                // 콘솔 허용
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                //일반 적인 로그인
                .formLogin(login -> login
                        .loginPage("/user/login") // 로그인 페이지 url
                        .loginProcessingUrl("/user/loginProcess") // 로그인 처리
                        .defaultSuccessUrl("/") // 성공시
                )
                //OAuth 로그인
                .oauth2Login(oauth -> oauth
                        .loginPage("/user/login") // 로그인 페이지 url
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(principalOauth2UserService)) // OAuth 가 들어오면 이 서비스로 매핑됨
                )
                // 로그아웃
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
