package com.hanghae0705.sbmoney.security;

import com.hanghae0705.sbmoney.security.handler.JwtAccessDeniedHandler;
import com.hanghae0705.sbmoney.security.handler.JwtAuthenticationEntryPoint;
import com.hanghae0705.sbmoney.security.jwt.JwtSecurityConfig;
import com.hanghae0705.sbmoney.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) //@Secured 어노테이션 활성화 - admin 만 접근 제한할 수 있음
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public BCryptPasswordEncoder  encodePassword(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //h2 대시보드 허용
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                //세션 사용 금지
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //접근권한 제어
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()

                //토큰 프로바이더 사용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedMethod("POST, GET, DELETE, PUT, PATCH, OPTIONS");
        configuration.addAllowedHeader("Content-Type, Authorization");
        configuration.addAllowedOriginPattern("http://localhost:3000");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/h2-console/**",
            "/api/user/**",
            "/test",
            "/**",
            "/**/**",
            "/**/**/**"
    };
}
