package com.github.suraj.ailibrary;

import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.repository.ContentPageRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class AiLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiLibraryApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ContentPageRepo repo) {
		return args -> {
			if (repo.count() == 0) {
				// Inserting JavaScript related queries
				repo.save(new ContentPage(null, "How to declare a variable in JavaScript?",
						"In JavaScript, you can declare a variable using the `var`, `let`, or `const` keywords. For example: `let variableName;`",
						"GPT-3", LocalDateTime.now(), "Yes", 5));

				repo.save(new ContentPage(null, "Explain JavaScript closures.",
						"A closure in JavaScript is a function that has access to the parent scope's variables, even after the parent function has closed.",
						"GPT-3", LocalDateTime.now(), "Yes", 5));

				repo.save(new ContentPage(null, "Difference between `==` and `===` in JavaScript?",
						"`==` checks for value equality with type coercion, while `===` checks for value equality without type coercion, ensuring that both the value and the type are the same.",
						"GPT-3", LocalDateTime.now(), "Yes", 5));

				System.out.println("Sample JavaScript data inserted!");
			}else{
				System.out.println("Data already exists. Skipping insertion.");
			}
		};
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin","Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
