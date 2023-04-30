package com.example.QuizWebApp.Model.Leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service class for leaderboard functionality.
 */
@Service
@Slf4j
public class LeaderboardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    /**
     * Saves a new leaderboard entry or updates an existing one with a higher score.
     *
     * @param playerName the name of the player
     * @param percentage the percentage of the score
     */
    public void saveLeaderboardEntry(String playerName, BigDecimal percentage) {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        LeaderboardEntry existingEntry = leaderboardRepository.findByPlayerName(playerName);

        if (existingEntry == null) {
            // Save new entry if playerName doesn't exist
            LeaderboardEntry entry = new LeaderboardEntry(playerName, percentage, formattedTimestamp);
            log.info("Saving new leaderboard entry: {}", entry);
            leaderboardRepository.save(entry);
        } else if (percentage.compareTo(existingEntry.getPercentage()) > 0) {
            // Update entry if playerName exists and the new score is higher
            existingEntry.setPercentage(percentage);
            existingEntry.setTimestamp(formattedTimestamp);
            log.info("Updating existing leaderboard entry: {}", existingEntry);
            leaderboardRepository.save(existingEntry);
        } else {
            log.info("Score is not higher than existing leaderboard entry, no update needed: {}", existingEntry);
        }
    }

    /**
     * Gets the top 10 leaderboard entries ordered by percentage in descending order and timestamp in ascending order.
     *
     * @return a page of leaderboard entries
     */
    public Page<LeaderboardEntry> getTop10LeaderboardEntries() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "percentage").and(Sort.by(Sort.Direction.ASC, "timestamp")));
        Page<LeaderboardEntry> top10Entries = leaderboardRepository.findTop10ByOrderByPercentageDescTimestampAsc(pageRequest);
        log.info("Fetching top 10 leaderboard entries: {}", top10Entries);
        return top10Entries;
    }
}
