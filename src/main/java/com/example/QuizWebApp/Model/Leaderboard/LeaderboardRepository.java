package com.example.QuizWebApp.Model.Leaderboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface provides database operations for the LeaderboardEntry entity using Spring Data JPA.
 */
@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    /**
     * Find the top 10 leaderboard entries by order of percentage in descending order and timestamp in ascending order.
     *
     * @param pageable the page request that defines the pagination and sorting of the result
     * @return a page of leaderboard entries that contains the top 10 entries
     */
    Page<LeaderboardEntry> findTop10ByOrderByPercentageDescTimestampAsc(Pageable pageable);

    /**
     * Find a leaderboard entry by player name.
     *
     * @param playerName the name of the player to search for
     * @return the leaderboard entry that matches the player name, or null if no match is found
     */
    LeaderboardEntry findByPlayerName(String playerName);
}

