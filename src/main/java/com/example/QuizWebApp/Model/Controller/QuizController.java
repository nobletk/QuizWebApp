package com.example.QuizWebApp.Model.Controller;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Quiz.QuizForm;
import com.example.QuizWebApp.Quiz.QuizResult;
import com.example.QuizWebApp.Quiz.QuizService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
//        String playerName = "Player"; // Replace this with the user's name or username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String playerName = auth.getName(); // Get the authenticated user's username

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

    @GetMapping("/login")
    public String login() {
        logger.info("User requested login page");
        return "login";
    }

//    @PostMapping("/login")
//    public String loginUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
//        logger.info("User is trying to log in: {}", username);
//
//        User user = userService.findByUsername(username);
//        if (user != null && userService.validateUserCredentials(username, password)) {
//            logger.info("User login successful: {}", username);
//            session.setAttribute("user", user);
//            return "redirect:/";
//        }
//
//        logger.warn("User login failed: {}", username);
//        return "login";
//    }

//    @PostMapping("/signup")
//    public String signupUser(@RequestParam("username") String username, @RequestParam("password") String password) {
//        logger.info("User is trying to sign up: {}", username);
//
//        User user = userService.registerUser(username, password);
//        if (user != null) {
//            logger.info("User signup successful: {}", username);
//            return "redirect:/login";
//        }
//
//        logger.warn("User signup failed: {}", username);
//        return "signup";
//    }
}
