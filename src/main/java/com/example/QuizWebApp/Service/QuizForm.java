package com.example.QuizWebApp.Service;

import com.example.QuizWebApp.Model.Questions.Question;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class QuizForm {
    @OneToMany(fetch = FetchType.EAGER)
    private List<Question> questions;

    public QuizForm() {
        questions = new ArrayList<>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

