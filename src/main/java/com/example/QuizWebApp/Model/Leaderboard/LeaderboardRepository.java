package com.example.QuizWebApp.Model.Leaderboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    Page<LeaderboardEntry> findTop10ByOrderByPercentageDescTimestampAsc(Pageable pageable);

    LeaderboardEntry findByPlayerName(String playerName);
}

