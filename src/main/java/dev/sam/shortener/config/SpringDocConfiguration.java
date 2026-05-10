package dev.sam.shortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpringDocConfiguration {
	AppProperties prop;

	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(
		new Info().title(prop.getSpringDoc().getTitle())
		.description(prop.getSpringDoc().getDescription())
		.version(prop.getSpringDoc().getVersion())
		).servers(
		List.of(
		new Server().url("http://localhost:8080").description("LOCAL"),
		new Server().url(prop.getSpringDoc().getProdServer()).description("PROD")
		)
		);
	}
}
