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
import org.springframework.web.bind.annotation.RequestParam;

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
        QuizResult quizResult = quizService.checkAnswers(quizForm.getQuestions());
        logger.info("User got {} correct answers out of {}", quizResult.getCorrectAnswers(), quizResult.getTotalQuestions());
        model.addAttribute("quizResult", quizResult);
        return "result";
    }


//
//    @PostMapping("/submitQuiz")
//    public String submitQuiz(@RequestParam String playerName, @RequestParam List<Integer> playerAnswers, Model model) {
//        // Retrieve questions and check answers
//        List<Question> questions = // Fetch questions based on IDs from userAnswers
//        int score = quizService.checkAnswers(questions, playerAnswers);
//
//        // Save leaderboard entry
//        quizService.saveLeaderboardEntry(playerName, score);
//
//        // Add results to model
//        model.addAttribute("score", score);
//        model.addAttribute("userName", playerName);
//        return "result";
//    }
//
//    @GetMapping("/leaderboard")
//    public String leaderboard(Model model) {
//        List<LeaderboardEntry> entries = quizService.getTop10LeaderboardEntries();
//        model.addAttribute("entries", entries);
//        return "leaderboard";
//    }
}
