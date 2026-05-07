package dev.sam.shortener.config;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GeoIPConfiguration {
	@Bean
	public DatabaseReader databaseReader() throws IOException {
		InputStream inputStream = new ClassPathResource("GeoLite2-Country/GeoLite2-Country.mmdb").getInputStream();
		return new DatabaseReader.Builder(inputStream).build();
	}
}
