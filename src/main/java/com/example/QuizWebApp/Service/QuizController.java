package com.example.QuizWebApp.Service;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Questions.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
public class QuizController {
    private final QuizService quizService;
    private final int questionCount;
    private static final Logger logger = LogManager.getLogger(QuizController.class);

    @Autowired
    public QuizController(QuizService quizService, @Value("${quiz.question-count}") int questionCount) {
        this.quizService = quizService;
        this.questionCount = questionCount;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/quiz")
    public String quiz(Model model) {
        logger.info("Fetching {} random questions for the quiz", questionCount);
        List<Question> questions = quizService.getRandomQuestions(questionCount);
        QuizForm quizForm = new QuizForm();
        quizForm.setQuestions(questions);
        model.addAttribute("quizForm", quizForm);
        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(@ModelAttribute QuizForm quizForm, Model model) {
        logger.info("Checking user answers");
        logger.debug("QuizForm: {}", quizForm);
        QuizResult quizResult = quizService.checkAnswers(quizForm.getQuestions());
        logger.info("User got {} correct answers out of {}", quizResult.getCorrectAnswers(), quizResult.getTotalQuestions());

        // Save the leaderboard entry
        String playerName = "Player"; // Replace this with the user's name or username
        BigDecimal percentage = BigDecimal.valueOf(quizResult.getPercentage()).setScale(2, RoundingMode.HALF_UP);
        quizService.saveLeaderboardEntry(playerName, percentage);

        model.addAttribute("quizResult", quizResult);
        return "result";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<LeaderboardEntry> leaderboardEntries = quizService.getTop10LeaderboardEntries();
        model.addAttribute("leaderboardEntries", leaderboardEntries);
        return "leaderboard";
    }
}
