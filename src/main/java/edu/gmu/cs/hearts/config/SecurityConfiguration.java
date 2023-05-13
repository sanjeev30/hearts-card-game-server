package edu.gmu.cs.hearts.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable
                )
                .cors(cors -> cors.
                        configurationSource(
                        request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(Arrays.asList("https://localhost:4200"));
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                            configuration.setAllowedHeaders(Arrays.asList("*"));
                            return configuration;
                        })
                )
                .authorizeHttpRequests(authorizeHttpRequests-> authorizeHttpRequests
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
                        ).frameOptions(HeadersConfigurer.FrameOptionsConfig::deny
                        ).httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .preload(true)
                                .maxAgeInSeconds(31536000)
                        ).xssProtection(xss -> xss
                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        ).contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self';")
                        )
                )
        .build();
    }
}
