package dev.sam.shortener.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BeanConfiguration {
    AppProperties props;
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
        cors.setAllowedOriginPatterns(List.of("*"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        cors.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    SecretKey secretKey() {
        byte[] key = props.getSecretKey().getBytes(StandardCharsets.UTF_8);
        if (key.length < 32) throw new IllegalArgumentException("Secret key length must be at least 32 characters");
        return new SecretKeySpec(key, MacAlgorithm.HS512.getName());
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        try {
            return configuration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("Cannot get authentication manager configuration", e);
        }
    }
}
