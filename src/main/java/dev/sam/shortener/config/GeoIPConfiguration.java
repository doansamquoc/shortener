package dev.sam.shortener.config;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

@Configuration
public class GeoIPConfiguration {
	@Bean
	DatabaseReader databaseReader() throws IOException {
		File database = new ClassPathResource("GeoLite2-Country/GeoLite2-Country.mmdb").getFile();
		return new DatabaseReader.Builder(database).build();
	}
}
