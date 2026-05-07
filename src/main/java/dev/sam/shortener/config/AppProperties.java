package dev.sam.shortener.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppProperties {
	String secretKey;
	int cleanupUrlsAfterDays;

	@NestedConfigurationProperty
	final SpringDoc springDoc = new SpringDoc();

	@Getter
	@Setter
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class SpringDoc {
		String title;
		String description;
		String prodServer;
		String version;
	}
}
