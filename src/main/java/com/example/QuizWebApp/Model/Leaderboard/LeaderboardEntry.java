package com.example.QuizWebApp.Model.Leaderboard;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String playerName;

    @Column(precision = 5, scale = 2)  // Update the column to store percentage values
    private BigDecimal percentage;

    private LocalDateTime timestamp;

    public LeaderboardEntry(String playerName, BigDecimal percentage, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.percentage = percentage;
        this.timestamp = timestamp;
    }
}

