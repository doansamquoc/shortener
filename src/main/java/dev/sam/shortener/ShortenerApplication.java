package dev.sam.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
public class ShortenerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShortenerApplication.class, args);
	}
}
