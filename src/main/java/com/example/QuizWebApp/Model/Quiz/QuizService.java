package com.example.QuizWebApp.Model.Quiz;

import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Model.Questions.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for handling quiz-related operations.
 */
@Service
@Slf4j
public class QuizService {

    private final QuestionRepository questionRepository;

    public QuizService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Fetches a specified number of random questions from the database.
     *
     * @param count the number of random questions to fetch
     * @return a list of randomly-selected questions
     */
    public List<Question> getRandomQuestions(int count) {
        log.info("Fetching {} random questions from the database", count);
        List<Question> randomQuestions = questionRepository.findRandomQuestions(count);
        log.debug("Fetched questions: {}", randomQuestions);
        return randomQuestions;
    }

    /**
     * Checks the answers to a list of questions and calculates the quiz result.
     *
     * @param questions the list of questions with user answers
     * @return a QuizResult object containing the number of correct answers and total questions
     */
    public QuizResult checkAnswers(List<Question> questions) {
        int correctAnswers = 0;
        for (Question question : questions) {
            if (question.getUserAnswerIndex() != null) {
                log.debug("Question ID: {}, User Answer Index: {}, Correct Answer Index: {}",
                        question.getId(), question.getUserAnswerIndex(), question.getCorrectAnswerIndex());
                if (question.getUserAnswerIndex().equals(question.getCorrectAnswerIndex())) {
                    correctAnswers++;
                }
            }
        }

        return new QuizResult(correctAnswers, questions.size());
    }
}
