package com.umanghome.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // Scores
    private int userScore;
    private int computerScore;

    // Turn scores
    private int userTurnScore;
    private int computerTurnScore;

    // Determine turn (odd = computer, even = user)
    private int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize scores and turns to zero
        userScore = 0;
        computerScore = 0;
        userTurnScore = 0;
        computerTurnScore = 0;
        turn = 0;
    }
}
