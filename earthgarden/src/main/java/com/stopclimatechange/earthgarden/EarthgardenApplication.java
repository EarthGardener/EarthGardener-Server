package com.stopclimatechange.earthgarden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EarthgardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(EarthgardenApplication.class, args);
	}

}
