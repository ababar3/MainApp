package com.project.mainapp.models;


/**
 * This class is used to save data for database only
 * if we do not use this class then we'll have to make many variables which would have to be
 * handled and then passed back etc
 * This class has no other use other than storing data from database for a short while
 */
public class DbModel {

    public String id;
    public String question;
    public String option1;
    public String option2;
    public String option3;
    //public String option4;
    public String totalScore;
    public String choice1;
    public String choice2;
    public String choice3;
    //public String choice4;
    public String type;
    public String answer;
    public boolean isDummyEntry;
    public byte selectIndex = -1;

    public DbModel() {
        question = "";
        option1 = "";
        option2 = "";
        option3 = "";
        //option4 = "";
        totalScore = "";
        choice1 = "";
        choice2 = "";
        choice3 = "";
        //choice4 = "";
        type = "";
        answer = "";
        isDummyEntry = true;
    }

    public DbModel(String id, String question, String option1, String option2, String option3,
                    String totalScore, String choice1, String choice2,
                   String choice3, String type, String answer,
                   int isDummyEntry) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        //this.option4 = option4;
        this.totalScore = totalScore;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        //this.choice4 = choice4;
        this.type = type;
        this.answer = answer;
        this.isDummyEntry = isDummyEntry == 1;
    }

}
