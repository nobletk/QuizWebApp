package com.example.QuizWebApp.Model.Controller;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Leaderboard.LeaderboardService;
import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Quiz.QuizForm;
import com.example.QuizWebApp.Quiz.QuizResult;
import com.example.QuizWebApp.Quiz.QuizService;
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

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/quiz")
    public String quiz(Model model) {
        log.info("Fetching {} random questions for the quiz", questionCount);
        List<Question> questions = quizService.getRandomQuestions(questionCount);
        QuizForm quizForm = new QuizForm();
        quizForm.setQuestions(questions);
        model.addAttribute("quizForm", quizForm);
        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(@ModelAttribute QuizForm quizForm, Model model) {
        log.info("Checking user answers");
        log.debug("QuizForm: {}", quizForm);
        QuizResult quizResult = quizService.checkAnswers(quizForm.getQuestions());
        log.info("User got {} correct answers out of {}", quizResult.getCorrectAnswers(), quizResult.getTotalQuestions());

        // Save the leaderboard entry
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String playerName = auth.getName();

        BigDecimal percentage = BigDecimal.valueOf(quizResult.getPercentage()).setScale(2, RoundingMode.HALF_UP);
        leaderboardService.saveLeaderboardEntry(playerName, percentage);

        model.addAttribute("quizResult", quizResult);
        return "result";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String playerName = auth.getName();

        Page<LeaderboardEntry> leaderboardEntries = leaderboardService.getTop10LeaderboardEntries();
        model.addAttribute("leaderboardEntries", leaderboardEntries);
        model.addAttribute("currentUsername", playerName);
        return "leaderboard";
    }

    @GetMapping("/login")
    public String login() {
        log.info("User requested login page");
        return "login";
    }
}
