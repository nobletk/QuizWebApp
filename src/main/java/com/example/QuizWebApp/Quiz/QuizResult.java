package com.example.QuizWebApp.Quiz;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuizResult {
    private int correctAnswers;
    private int totalQuestions;

    public QuizResult(int correctAnswers, int totalQuestions) {
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
    }

    public double getPercentage() {
        return ((double) correctAnswers / totalQuestions) * 100;
    }
}

