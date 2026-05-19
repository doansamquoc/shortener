package dev.sam.shortener.config;

import dev.sam.shortener.resolver.CurrentUserIdArgumentResolver;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CurrentUserIdArgumentResolver());
	}
}
