package com.example.QuizWebApp;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Leaderboard.LeaderboardService;
import com.example.QuizWebApp.Model.Questions.Question;
import com.example.QuizWebApp.Model.Quiz.QuizController;
import com.example.QuizWebApp.Model.Quiz.QuizForm;
import com.example.QuizWebApp.Model.Quiz.QuizResult;
import com.example.QuizWebApp.Model.Quiz.QuizService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(QuizController.class)
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @MockBean
    private LeaderboardService leaderboardService;

    @Test
    @WithMockUser
    public void testHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser
    public void testQuiz() throws Exception {
        List<Question> questions = Arrays.asList(new Question(), new Question());

        when(quizService.getRandomQuestions(any(Integer.class))).thenReturn(questions);

        mockMvc.perform(post("/quiz")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("quizForm"))
                .andExpect(view().name("quiz"));
    }

    @Test
    @WithMockUser
    public void testSubmitQuiz() throws Exception {
        List<Question> questions = Arrays.asList(new Question(), new Question());
        QuizForm quizForm = new QuizForm();
        quizForm.setQuestions(questions);

        when(quizService.checkAnswers(any(List.class))).thenReturn(new QuizResult(1, 2));

        mockMvc.perform(post("/submitQuiz")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("quizForm", quizForm))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("quizResult"))
                .andExpect(view().name("result"));
    }

    @Test
    @WithMockUser
    public void testLeaderboard() throws Exception {
        List<LeaderboardEntry> leaderboardEntries = Arrays.asList(new LeaderboardEntry(), new LeaderboardEntry());
        PageImpl<LeaderboardEntry> page = new PageImpl<>(leaderboardEntries);

        when(leaderboardService.getTop10LeaderboardEntries()).thenReturn(page);

        mockMvc.perform(get("/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("leaderboardEntries"))
                .andExpect(model().attributeExists("currentUsername"))
                .andExpect(view().name("leaderboard"));
    }

    @Test
    @WithAnonymousUser
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("<title>Please sign in</title>")));
    }
}
