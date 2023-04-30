package com.example.QuizWebApp.Model.Questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for the Question entity.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {
    /**
     * Fetches a specified number of random questions from the database.
     *
     * @param count the number of random questions to fetch
     * @return a list of randomly-selected questions
     */
    @Query(value = "SELECT * FROM question ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<Question> findRandomQuestions(int count);
}
