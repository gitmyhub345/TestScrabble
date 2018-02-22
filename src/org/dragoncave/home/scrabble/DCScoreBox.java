/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import javafx.scene.control.Label;

/**
 *
 * @author William J
 */
public class DCScoreBox extends Label {
    private final String strTotalScore = "Total score: ";
    private final String strPlayScore = "Word score: ";
    private final String strAverageScore = "Average: ";
    private int score;
    private int totalScore;
    private int averageScore;
    private int numPlays;

    protected void setScore(int score) {
        this.score = score;
    }

    protected void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    public void setPlayScore(int playScore){
        score = playScore;
        totalScore += playScore;
        ++numPlays;
        averageScore = totalScore / numPlays;
        displayText();
    }
    
    public DCScoreBox(){
        score = 0;
        totalScore = 0;
        displayText();
    }
    
    public void resetScore(){
        score = 0;
        totalScore = 0;
        averageScore = 0;
        numPlays = 0;
        displayText();
    }
    
    private void displayText(){
        super.setText(strTotalScore+totalScore+"\n"+strPlayScore+score+"\n"+strAverageScore+averageScore);
    }
}
