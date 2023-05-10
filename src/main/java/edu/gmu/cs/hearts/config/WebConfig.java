package edu.gmu.cs.hearts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${client.url}")
    private String clientOrigin;

    @Value("${client.api-url-pattern}")
    private String apiURLPattern;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(apiURLPattern)
                .allowedOrigins(clientOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
