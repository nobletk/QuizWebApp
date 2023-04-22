package com.example.QuizWebApp.Service;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Leaderboard.LeaderboardRepository;
import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Model.Questions.QuestionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {
    private static final Logger logger = LogManager.getLogger(QuizService.class);
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private LeaderboardRepository leaderboardRepository;

    public List<Question> getRandomQuestions(int count) {
        logger.info("Fetching {} random questions from the database", count);
        List<Question> randomQuestions = questionRepository.findRandomQuestions(count);
        logger.debug("Fetched questions: {}", randomQuestions);
        return randomQuestions;
    }

    public QuizResult checkAnswers(List<Question> questions) {
        int correctAnswers = 0;
        for (Question question : questions) {
            if (question.getUserAnswerIndex() != null) {
                logger.debug("Question ID: {}, User Answer Index: {}, Correct Answer Index: {}",
                        question.getId(), question.getUserAnswerIndex(), question.getCorrectAnswerIndex());
                if (question.getUserAnswerIndex().equals(question.getCorrectAnswerIndex())) {
                    correctAnswers++;
                }
            }
        }

        return new QuizResult(correctAnswers, questions.size());
    }

    public void saveLeaderboardEntry(String playerName, BigDecimal percentage) {
        LocalDateTime timestamp = LocalDateTime.now();
        LeaderboardEntry entry = new LeaderboardEntry(playerName, percentage, timestamp);
        leaderboardRepository.save(entry);
    }

    public List<LeaderboardEntry> getTop10LeaderboardEntries() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "percentage").and(Sort.by(Sort.Direction.ASC, "timestamp")));
        return leaderboardRepository.findTop10ByOrderByPercentageDescTimestampAsc(pageRequest);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }
}

