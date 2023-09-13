package com.github.suraj.ailibrary;

import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.repository.ContentPageRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

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

}
