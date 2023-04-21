package com.example.QuizWebApp.Model.Questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM question ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<Question> findRandomQuestions(int count);
}
