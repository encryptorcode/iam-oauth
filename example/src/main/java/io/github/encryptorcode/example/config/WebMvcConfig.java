package io.github.encryptorcode.example.config;

import io.github.encryptorcode.implementation.spring.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"io.github.encryptorcode.example.controllers", "io.github.encryptorcode.implementation.spring"})
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthenticationInterceptor());
    }

    @Bean
    public HandlerInterceptor getAuthenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
