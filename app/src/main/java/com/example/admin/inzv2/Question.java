package com.example.admin.inzv2;

import java.util.ArrayList;

/**
 * Created by Admin on 2016-08-21.
 */
public class Question {

   private String question_line , anserw_line;

    public Question(String question_line, String anserw_line) {
        this.question_line = question_line;
        this.anserw_line = anserw_line;
    }

    public String getQuestion_line() {
        return question_line;
    }

    public void setQuestion_line(String question_line) {
        this.question_line = question_line;
    }

    public String getAnserw_line() {
        return anserw_line;
    }

    public void setAnserw_line(String anserw_line) {
        this.anserw_line = anserw_line;
    }

    public static class List extends ArrayList<Question> {

    }
}
