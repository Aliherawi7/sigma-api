package com.herawi.sigma.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Value("${allowed.origin}")
    private String allowedOrigin;
    @Bean
    public WebMvcConfigurer getCorsWebMvcConfiguration(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(allowedOrigin)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.getAllowCredentials();
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedHeader("*");
        config.setAllowedOrigins(Arrays.asList(allowedOrigin,allowedOrigin+"*"));
        config.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
