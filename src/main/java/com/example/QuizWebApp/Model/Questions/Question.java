package com.example.QuizWebApp.Model.Questions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Class representing a question entity in the quiz.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;

    @Column(name = "correct_answer_index")
    private int correctAnswerIndex;

    @Transient
    private Integer userAnswerIndex;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", options=" + options +
                ", correctAnswerIndex=" + correctAnswerIndex +
                ", userAnswerIndex=" + userAnswerIndex +
                '}';
    }

}
