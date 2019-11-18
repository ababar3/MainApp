package com.project.mainapp.models;

/**
 * Simple class to store the data for time being about the score of the question and the type of
 * it as well
 */

public class ScoreModel {
    public int earnedScore;
    public int totalScore;
    public String type;


    public ScoreModel(int earnedScore, int totalScore, String type) {
        this.earnedScore = earnedScore;
        this.totalScore = totalScore;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ScoreModel{" +
                "earnedScore=" + earnedScore +
                ", totalScore=" + totalScore +
                ", type='" + type + '\'' +
                '}';
    }
}
