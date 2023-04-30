package com.example.QuizWebApp.Model.Leaderboard;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * This class represents an entry in the leaderboard, which stores the player name, their percentage score, and the timestamp of the score update.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String playerName;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentage;

    private String timestamp;

    /**
     * Constructs a leaderboard entry with the specified player name, percentage score, and timestamp.
     *
     * @param playerName the name of the player
     * @param percentage the percentage score of the player
     * @param timestamp  the timestamp of the score update
     */
    public LeaderboardEntry(String playerName, BigDecimal percentage, String timestamp) {
        this.playerName = playerName;
        this.percentage = percentage;
        this.timestamp = timestamp;
    }
}

