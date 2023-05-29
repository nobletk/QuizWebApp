package com.example.QuizWebApp.Model.Quiz;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing the result of a quiz.
 */
@Getter
@Setter
@NoArgsConstructor
public class QuizResult {
    private int correctAnswers;
    private int totalQuestions;

    /**
     * Constructs a QuizResult with the specified number of correct answers and total questions.
     *
     * @param correctAnswers the number of correct answers
     * @param totalQuestions the total number of questions in the quiz
     */
    public QuizResult(int correctAnswers, int totalQuestions) {
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
    }

    /**
     * Calculates and returns the percentage of correct answers in the quiz.
     *
     * @return the percentage of correct answers as a double value
     */
    public double getPercentage() {
        return ((double) correctAnswers / totalQuestions) * 100;
    }
}

