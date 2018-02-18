package com.example.android.wir_tecrepo.miscellaneous;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

public class CourtCounter extends AppCompatActivity{
    private int scoreA = 0;
    private int scoreB = 0;
    //private final int POINTS_FOR_FREE_THROW = 1;

    @Override // onCreate method executes when your app starts up
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.court_counter);
    }

    /**
     * Increase the score for Team A by 3 points.
     * @param v
     */
    public void addThreeForTeamA(View v){
        scoreA += 3;
        displayForTeamA(scoreA);
    }

    /**
     * Increase the score for Team A by 2 points.
     * @param v
     */
    public void addTwoForTeamA(View v){
        scoreA += 2;
        displayForTeamA(scoreA);
    }

    /**
     * Increase the score for Team A by the free throw points.
     * @param v
     */
    public void addOneForTeamA(View v){
        scoreA ++;
        displayForTeamA(scoreA);
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    //######################################### TEAM B ####################################

    /**
     * Increase the score for Team B by 3 points.
     * @param v
     */
    public void addThreeForTeamB(View v){
        scoreB += 3;
        displayForTeamB(scoreB);
    }

    /**
     * Increase the score for Team B by 2 points.
     * @param v
     */
    public void addTwoForTeamB(View v){
        scoreB += 2;
        displayForTeamB(scoreB);
    }

    /**
     * Increase the score for Team B by the free throw points.
     * @param v
     */
    public void addOneForTeamB(View v){
        scoreB ++;
        displayForTeamB(scoreB);
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    public void resetScores(View v){
        scoreA = scoreB = 0;
        displayForTeamA(scoreA);
        displayForTeamB(scoreB);
    }
}
