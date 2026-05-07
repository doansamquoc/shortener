package dev.sam.shortener.config;

import dev.sam.shortener.config.jwt.JwtAccessDeniedHandler;
import dev.sam.shortener.config.jwt.JwtAuthenticationEntryPoint;
import dev.sam.shortener.config.jwt.JwtConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static dev.sam.shortener.constant.EndpointConstant.PUBLIC_ENDPOINTS;
import static dev.sam.shortener.constant.EndpointConstant.SWAGGER_ENDPOINTS;

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

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults());
		http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
			auth.requestMatchers(SWAGGER_ENDPOINTS).permitAll();
			auth.anyRequest().authenticated();
		});
		http.oauth2ResourceServer(oauth2 -> {
			oauth2.authenticationEntryPoint(authenticationEntryPoint);
			oauth2.accessDeniedHandler(accessDeniedHandler);
			oauth2.jwt(jwt -> {
				jwt.decoder(jwtDecoder);
				jwt.jwtAuthenticationConverter(jwtConverter);
			});
		});
		return http.build();
	}
}
