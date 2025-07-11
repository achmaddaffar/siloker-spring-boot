package com.oliver.siloker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliver.siloker.component.AuthFilter;
import com.oliver.siloker.model.response.ErrorDto;
import com.oliver.siloker.model.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] ALLOWED = {
            "/api/v1/ping",
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/images/**"
    };

    private final AuthFilter authFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(ALLOWED)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getWriter().write(
                                            objectMapper.writeValueAsString(
                                                    new BaseResponse<>(
                                                            HttpStatus.UNAUTHORIZED.value(),
                                                            "Unauthorized access",
                                                            new ErrorDto(
                                                                    HttpStatus.UNAUTHORIZED.value(),
                                                                    "UNAUTHORIZED",
                                                                    authException.getLocalizedMessage()
                                                            )
                                                    )
                                            )
                                    );
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getWriter().write(
                                            objectMapper.writeValueAsString(
                                                    new BaseResponse<>(
                                                            HttpStatus.FORBIDDEN.value(),
                                                            "Access Denied",
                                                            new ErrorDto(
                                                                    HttpStatus.FORBIDDEN.value(),
                                                                    "FORBIDDEN",
                                                                    accessDeniedException.getLocalizedMessage()
                                                            )
                                                    )
                                            )
                                    );
                                })
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

