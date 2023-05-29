package com.example.QuizWebApp;

import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Model.Questions.QuestionRepository;
import com.example.QuizWebApp.Model.Quiz.QuizResult;
import com.example.QuizWebApp.Model.Quiz.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuizService quizService;

    private Question question1;
    private Question question2;

    @BeforeEach
    public void setUp() {
        question1 = new Question();
        question1.setId(1L);
        question1.setQuestionText("Question 1");
        question1.setCorrectAnswerIndex(0);

        question2 = new Question();
        question2.setId(2L);
        question2.setQuestionText("Question 2");
        question2.setCorrectAnswerIndex(1);
    }

    @Test
    public void testGetRandomQuestions() {

        int count = 2;
        when(questionRepository.findRandomQuestions(count)).thenReturn(Arrays.asList(question1, question2));


        List<Question> result = quizService.getRandomQuestions(count);


        assertEquals(2, result.size());
        assertEquals(question1, result.get(0));
        assertEquals(question2, result.get(1));
        verify(questionRepository, times(1)).findRandomQuestions(count);
    }

    @Test
    public void testCheckAnswers() {

        question1.setUserAnswerIndex(0); // Correct answer
        question2.setUserAnswerIndex(2); // Incorrect answer
        List<Question> questions = Arrays.asList(question1, question2);


        QuizResult result = quizService.checkAnswers(questions);

        assertEquals(1, result.getCorrectAnswers());
        assertEquals(2, result.getTotalQuestions());
    }
}

