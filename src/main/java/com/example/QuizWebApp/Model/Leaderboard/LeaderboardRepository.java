package com.example.QuizWebApp.Model.Leaderboard;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    List<LeaderboardEntry> findTop10ByOrderByPercentageDescTimestampAsc(Pageable pageable);
}

