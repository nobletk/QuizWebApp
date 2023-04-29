package com.example.QuizWebApp;

import com.example.QuizWebApp.Quiz.QuizResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizResultTest {

    @Test
    public void testGetPercentage() {

        QuizResult quizResult = new QuizResult(5, 10);

        double percentage = quizResult.getPercentage();

        assertEquals(50.0, percentage);
    }
}
