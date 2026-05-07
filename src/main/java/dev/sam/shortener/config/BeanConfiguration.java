package dev.sam.shortener.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanConfiguration {
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
		cors.setAllowedOrigins(List.of("http://localhost:*"));
		cors.setAllowedHeaders(List.of("*"));
		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		cors.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);
		return source;
	}
}
