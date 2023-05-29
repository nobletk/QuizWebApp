package com.example.QuizWebApp.Model.Quiz;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Leaderboard.LeaderboardService;
import com.example.QuizWebApp.Model.Questions.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * The QuizController class is a Spring MVC controller that handles the quiz-related functionality of the application,
 * including displaying and submitting quizzes, displaying the leaderboard, and logging in.
 */
@Controller
@Slf4j
public class QuizController {
    private final QuizService quizService;
    private final LeaderboardService leaderboardService;
    private final int questionCount;

    @Autowired
    public QuizController(QuizService quizService, LeaderboardService leaderboardService, @Value("${quiz.question-count}") int questionCount) {
        this.quizService = quizService;
        this.leaderboardService = leaderboardService;
        this.questionCount = questionCount;
    }

    /**
     * Handles the GET request to the home page.
     *
     * @return the home page view
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Handles the POST request to start the quiz.
     *
     * @param model the model object to add attributes to
     * @return the quiz page view
     */
    @PostMapping("/quiz")
    public String quiz(Model model) {
        log.info("Fetching {} random questions for the quiz", questionCount);
        List<Question> questions = quizService.getRandomQuestions(questionCount);
        QuizForm quizForm = new QuizForm();
        quizForm.setQuestions(questions);
        model.addAttribute("quizForm", quizForm);
        return "quiz";
    }

    /**
     * Handles the POST request to submit the quiz.
     *
     * @param quizForm the quiz form containing the user's answers
     * @param model    the model object to add attributes to
     * @return the result page view
     */
    @PostMapping("/submitQuiz")
    public String submitQuiz(@ModelAttribute QuizForm quizForm, Model model) {
        log.info("Checking user answers");
        log.debug("QuizForm: {}", quizForm);
        QuizResult quizResult = quizService.checkAnswers(quizForm.getQuestions());
        log.info("User got {} correct answers out of {}", quizResult.getCorrectAnswers(), quizResult.getTotalQuestions());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String playerName = auth.getName();

        BigDecimal percentage = BigDecimal.valueOf(quizResult.getPercentage()).setScale(2, RoundingMode.HALF_UP);
        leaderboardService.saveLeaderboardEntry(playerName, percentage);

        model.addAttribute("quizResult", quizResult);
        return "result";
    }

    /**
     * Handles the GET request to display the leaderboard.
     *
     * @param model the model object to add attributes to
     * @return the leaderboard page view
     */
    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String playerName = auth.getName();

        Page<LeaderboardEntry> leaderboardEntries = leaderboardService.getTop10LeaderboardEntries();
        model.addAttribute("leaderboardEntries", leaderboardEntries);
        model.addAttribute("currentUsername", playerName);
        return "leaderboard";
    }
}
