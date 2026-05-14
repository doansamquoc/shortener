package dev.sam.shortener.config;

import dev.sam.shortener.config.jwt.JwtAccessDeniedHandler;
import dev.sam.shortener.config.jwt.JwtAuthenticationEntryPoint;
import dev.sam.shortener.config.jwt.JwtConverter;
import dev.sam.shortener.enums.Role;
import dev.sam.shortener.service.OAuth2FailureHandler;
import dev.sam.shortener.service.OAuth2SuccessHandler;
import dev.sam.shortener.service.CustomOAuth2UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfigurationSource;

import static dev.sam.shortener.constant.EndpointConstant.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfiguration {
    JwtDecoder jwtDecoder;
    JwtConverter jwtConverter;
    JwtAuthenticationEntryPoint authenticationEntryPoint;
    JwtAccessDeniedHandler accessDeniedHandler;
    CorsConfigurationSource corsConfigurationSource;
    
    CustomOAuth2UserService customOAuth2UserService;
    OAuth2FailureHandler oAuth2FailureHandler;
    OAuth2SuccessHandler oAuth2SuccessHandler;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource));
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
            auth.requestMatchers(HttpMethod.POST, "/api/v1/urls").permitAll();
            auth.requestMatchers(ADMIN_ENDPOINTS).hasAuthority(Role.ROLE_ADMIN.name());
            auth.requestMatchers(SWAGGER_ENDPOINTS).permitAll();
            auth.anyRequest().authenticated();
        });
        http.exceptionHandling(e -> e
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );
        http.oauth2ResourceServer(oauth2 -> {
            oauth2.authenticationEntryPoint(authenticationEntryPoint);
            oauth2.accessDeniedHandler(accessDeniedHandler);
            oauth2.jwt(jwt -> {
                jwt.decoder(jwtDecoder);
                jwt.jwtAuthenticationConverter(jwtConverter);
            });
        });
        http.oauth2Login(oauth2 -> {
            oauth2.userInfoEndpoint(u -> u.userService(customOAuth2UserService));
            oauth2.successHandler(oAuth2SuccessHandler);
            oauth2.failureHandler(oAuth2FailureHandler);
        });
        return http.build();
    }
}
