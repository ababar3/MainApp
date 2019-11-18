package com.project.mainapp.models;

import java.util.ArrayList;

public class UnattemptedModel {

    public String type;
    private String questions;


    public void setQuestions(ArrayList<DbModel> data){
        StringBuilder sb = new StringBuilder();

        for(DbModel d: data){
            sb.append(d.question).append("\n");
        }

        questions = sb.toString();
    }

    public String getQuestions(){
        return questions;
    }
}
