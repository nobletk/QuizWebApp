package com.example.QuizWebApp;

import com.example.QuizWebApp.Model.Leaderboard.LeaderboardEntry;
import com.example.QuizWebApp.Model.Leaderboard.LeaderboardRepository;
import com.example.QuizWebApp.Model.Leaderboard.LeaderboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Mock
    private LeaderboardRepository leaderboardRepository;

    private LeaderboardEntry leaderboardEntry;

    @BeforeEach
    public void setUp() {
        leaderboardEntry = new LeaderboardEntry("player1", BigDecimal.valueOf(75.5), "2023-04-30 10:00:00");
    }

    @Test
    public void testSaveLeaderboardEntry_NewEntry() {

        when(leaderboardRepository.findByPlayerName("player1")).thenReturn(null);

        leaderboardService.saveLeaderboardEntry("player1", BigDecimal.valueOf(75.5));

        verify(leaderboardRepository, times(1)).save(any(LeaderboardEntry.class));
    }

    @Test
    public void testSaveLeaderboardEntry_ScoreHigher() {

        when(leaderboardRepository.findByPlayerName("player1")).thenReturn(leaderboardEntry);

        leaderboardService.saveLeaderboardEntry("player1", BigDecimal.valueOf(85.5));

        verify(leaderboardRepository, times(1)).save(any(LeaderboardEntry.class));
    }

    @Test
    public void testSaveLeaderboardEntry_ScoreLower() {

        when(leaderboardRepository.findByPlayerName("player1")).thenReturn(leaderboardEntry);

        leaderboardService.saveLeaderboardEntry("player1", BigDecimal.valueOf(70.0));

        verify(leaderboardRepository, never()).save(any(LeaderboardEntry.class));
    }

    @Test
    public void testSaveLeaderboardEntry_SameScore() {

        when(leaderboardRepository.findByPlayerName("player1")).thenReturn(leaderboardEntry);

        leaderboardService.saveLeaderboardEntry("player1", BigDecimal.valueOf(75.5));

        verify(leaderboardRepository, never()).save(any(LeaderboardEntry.class));
    }


    @Test
    public void testGetTop10LeaderboardEntries() {

        List<LeaderboardEntry> entriesList = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            entriesList.add(new LeaderboardEntry("player" + i, BigDecimal.valueOf(75.5 - i), "2023-04-30 10:00:0" + i));
        }

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "percentage").and(Sort.by(Sort.Direction.ASC, "timestamp")));
        Page<LeaderboardEntry> leaderboardEntries = new PageImpl<>(entriesList.subList(0, 10), pageRequest, 11);
        when(leaderboardRepository.findTop10ByOrderByPercentageDescTimestampAsc(pageRequest)).thenReturn(leaderboardEntries);

        Page<LeaderboardEntry> result = leaderboardService.getTop10LeaderboardEntries();

        assertEquals(11, result.getTotalElements());
        assertEquals(entriesList.subList(0, 10), result.getContent());
        verify(leaderboardRepository, times(1)).findTop10ByOrderByPercentageDescTimestampAsc(pageRequest);
    }
}
