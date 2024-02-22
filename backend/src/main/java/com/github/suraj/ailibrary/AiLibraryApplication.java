package com.github.suraj.ailibrary;

import com.github.suraj.ailibrary.model.ContentPages.Category;
import com.github.suraj.ailibrary.model.ContentPages.ContentPage;
import com.github.suraj.ailibrary.model.ContentPages.PageOrderEntry;
import com.github.suraj.ailibrary.repository.CategoryRepo;
import com.github.suraj.ailibrary.repository.ContentPageRepo;

import com.github.suraj.ailibrary.repository.PageOrderEntryRepo;
import org.springframework.beans.factory.annotation.Value;
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
	public CommandLineRunner run(ContentPageRepo contentPageRepo, CategoryRepo categoryRepo, PageOrderEntryRepo pageOrderEntryRepo) {
		return args -> {

			if (contentPageRepo.count() == 0) {
				// 1. Create and Save Categories
				Category javaCategory = new Category(null, "Java");
				Category jsCategory = new Category(null, "JavaScript");
				categoryRepo.saveAll(Arrays.asList(javaCategory, jsCategory));

				// 2. Create Content Pages
				ContentPage jsPage1 = new ContentPage(null, "How to declare a variable in JavaScript?", "var name = value;", "GPT-3", LocalDateTime.now(), "Yes", 5);
				ContentPage jsPage2 = new ContentPage(null, "Explain JavaScript closures.", "...", "GPT-3", LocalDateTime.now(), "Yes", 5);
				contentPageRepo.saveAll(Arrays.asList(jsPage1, jsPage2));

				// 3. Create Page Order Entries
				PageOrderEntry order1 = new PageOrderEntry(null, javaCategory, jsPage1, 0);
				PageOrderEntry order2 = new PageOrderEntry(null, jsCategory, jsPage2, 1);
				pageOrderEntryRepo.saveAll(Arrays.asList(order1, order2));


				System.out.println("Sample data inserted!");
			} else {
				System.out.println("Data already exists. Skipping insertion.");
			}
		};
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false); // If you need cookies, etc.
		config.setAllowedOrigins(Arrays.asList(
				"http://localhost:3000",
				"http://localhost:4200",
				"https://www.neverforgetnotebook.com",
				"https://neverforgetnotebook.com"
		));
		config.setAllowedHeaders(Arrays.asList(
				"Origin",
				"Content-Type",
				"Accept",
				"Jwt-Token",
				"Authorization",
				"X-Requested-With",
				"Access-Control-Request-Method",
				"Access-Control-Request-Headers"
		));
		config.setExposedHeaders(Arrays.asList(
				"Content-Type",
				"Accept",
				"Jwt-Token",
				"Authorization",
				"Filename"
		));
		config.setAllowedMethods(Arrays.asList(
				"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
		));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}


}
