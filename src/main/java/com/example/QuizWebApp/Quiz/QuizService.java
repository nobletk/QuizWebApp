package com.example.QuizWebApp.Quiz;

import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Model.Questions.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getRandomQuestions(int count) {
        log.info("Fetching {} random questions from the database", count);
        List<Question> randomQuestions = questionRepository.findRandomQuestions(count);
        log.debug("Fetched questions: {}", randomQuestions);
        return randomQuestions;
    }

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

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }
}


