package com.example.QuizWebApp.Model.Quiz;

import com.example.QuizWebApp.Model.Questions.Question;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a quiz form containing a list of questions.
 */
@Getter
@Setter
public class QuizForm {
    @OneToMany(fetch = FetchType.EAGER)
    private List<Question> questions;

    public QuizForm() {
        questions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "QuizForm{" +
                "questions=" + questions +
                '}';
    }
}


