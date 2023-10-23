package com.github.suraj.ailibrary;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;
import com.github.suraj.ailibrary.repository.CategoryRepo;
import com.github.suraj.ailibrary.repository.ContentPageRepo;

import com.github.suraj.ailibrary.repository.PageOrderEntryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.time.LocalDateTime;
import java.util.ArrayList;
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
				Category javaCategory = new Category(null, "Java", new ArrayList<>());
				Category jsCategory = new Category(null, "JavaScript", new ArrayList<>());
				categoryRepo.saveAll(Arrays.asList(javaCategory, jsCategory));

				// 2. Create Content Pages
				ContentPage jsPage1 = new ContentPage(null, "How to declare a variable in JavaScript?", "...", "GPT-3", LocalDateTime.now(), "Yes", 5, new ArrayList<>());
				ContentPage jsPage2 = new ContentPage(null, "Explain JavaScript closures.", "...", "GPT-3", LocalDateTime.now(), "Yes", 5, new ArrayList<>());
				contentPageRepo.saveAll(Arrays.asList(jsPage1, jsPage2));

				// 3. Create Page Order Entries
				PageOrderEntry order1 = new PageOrderEntry(null, javaCategory, jsPage1, 0);
				PageOrderEntry order2 = new PageOrderEntry(null, jsCategory, jsPage2, 1);
				pageOrderEntryRepo.saveAll(Arrays.asList(order1, order2));

				// 4. Add Page Order Entries to Content Pages and Save Again
				jsPage1.getOrderings().add(order1);
				jsPage2.getOrderings().add(order2);
				contentPageRepo.saveAll(Arrays.asList(jsPage1, jsPage2));

				System.out.println("Sample data inserted!");
			} else {
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
