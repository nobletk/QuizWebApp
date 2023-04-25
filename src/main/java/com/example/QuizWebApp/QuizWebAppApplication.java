package com.example.QuizWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuizWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizWebAppApplication.class, args);
	}

//	@Bean
//	CommandLineRunner initData(QuizService quizService) {
//		return args -> {
//			Question question1 = new Question(
//					"How many soccer players should be on the field at the same time?",
//					Arrays.asList("20", "22", "24", "26"),
//					1
//			);
//
//			quizService.saveQuestion(question1);
//		};
//	}
}
