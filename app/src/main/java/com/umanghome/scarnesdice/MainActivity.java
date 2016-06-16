package com.umanghome.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Random
    Random random;

    // Scores
    private int userScore;
    private int computerScore;

    // Turn scores
    private int userTurnScore;
    private int computerTurnScore;

    // Determine turn (odd = computer, even = user)
    private int turn;

    // Create UI elements
    private TextView scoreTextView;
    private TextView turnTextView;
    private ImageView diceImageView;
    private Button rollButton;
    private Button holdButton;
    private Button resetButton;

    private final Handler handler = new Handler();
    private Runnable computerTurnRunnable;

    // Boolean to keep track of whether the game is over
    private boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Random
        random = new Random();

        // Initialize UI elements
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        turnTextView = (TextView) findViewById(R.id.turnTextView);
        diceImageView = (ImageView) findViewById(R.id.diceImageView);
        rollButton = (Button) findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        // Initialize variables
        reset();

        computerTurnRunnable = new Runnable () {
            @Override
            public void run () {
                computerTurn();
            }
        };

        // Action to do when rollButton is clicked
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoll();
            }
        });

        // Action to do when holdButton is clicked
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Don't process if the user hasn't rolled the dice yet
                if (userTurnScore == 0) {
                    return;
                }

                // Add round score to game score
                addUsersScore();

                // Computer's turn now
                turn++;
                computerTurn();

            }
        });

        // Action to do when resetButton is clicked
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset game variables
                reset();

                // Enable the buttons
                rollButton.setEnabled(true);
                holdButton.setEnabled(true);
            }
        });

    }

    // Reset game variables
    private void reset () {
        userScore = 0;
        computerScore = 0;
        userTurnScore = 0;
        computerTurnScore = 0;
        turn = 0;
        gameOver = false;

        // Update the strings
        setTurnString();
        setScoreString();
    }

    // User's turn
    public void userRoll () {

        // Generate a number
        int number = random.nextInt(6) + 1;

        // Set image
        setDiceImage(number);

        // BOO
        if (number == 1) {
            // Reset round turn
            userTurnScore = 0;

            // Computer's turn
            turn++;
            computerTurn();
        }
        // YAY
        else {
            // Add die number to round score
            userTurnScore += number;

            // Did the user win?
            if ((userScore + userTurnScore) >= 100) {
                // YAY
                addUsersScore();
                endGame(true);
            }
        }

        // Update string
        setTurnString();

    }

    // Update user's score
    public void addUsersScore () {
        // Add round score to game score
        userScore += userTurnScore;

        // Update strings
        setTurnString();
        setScoreString();

        // Reset round score
        userTurnScore = 0;
    }

    // Computer's turn
    public void computerTurn () {

        // Disable buttons
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        // Roll the die
        int number = random.nextInt(6) + 1;
        setDiceImage(number);

        // BOO
        if (number == 1) {
            computerTurnScore = 0;
            turn++;
        }
        // YAY
        else {
            // Add die roll to turn score
            computerTurnScore += number;

            // Did the computer win?
            if ((computerTurnScore + computerScore) >= 100) {

                // Yes
                addComputersScore();
                // If game isn't already over, end it
                if (!gameOver) {
                    endGame(false);
                }
            }

            // Strategy: If turn score is less than 20, roll. Hold otherwise.
            if (computerTurnScore < 20) {
                // Roll
                handler.postDelayed(computerTurnRunnable, 1000);
//                computerTurn();
            } else {
                // Hold
                addComputersScore();
                turn++;
            }
        }

        // Update string
        setTurnString();

        // Enable buttons if game isn't over
        if (!gameOver) {
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
        }
    }

    // Update computer's score
    private void addComputersScore () {
        // Add round score to game score
        computerScore += computerTurnScore;

        // Update strings
        setTurnString();
        setScoreString();

        // Reset round score
        computerTurnScore = 0;
    }

    // End game
    // User won if won => true
    // Computer won if won => false
    private void endGame (boolean won) {

        // Initialize text for Toast
        String toastText = "";

        // Set text for Toast
        if (won) {
            toastText = "Yay! You won!";
        } else {
            toastText = "Sorry, the computer won!";
        }

        // Disable buttons
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        // Game is over now
        gameOver = true;

        // Display Toast
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    // Set die image
    private void setDiceImage (int number) {
        // Get image id
        int id;
        switch (number) {
            case 1: id = R.drawable.dice1; break;
            case 2: id = R.drawable.dice2; break;
            case 3: id = R.drawable.dice3; break;
            case 4: id = R.drawable.dice4; break;
            case 5: id = R.drawable.dice5; break;
            case 6: id = R.drawable.dice6; break;
            default: id = R.drawable.dice1; break;
        }

        // Set image
        diceImageView.setImageDrawable(getResources().getDrawable(id, getTheme()));
    }

    // Sets score string
    private void setScoreString () {
        String score = "Your score: " + userScore
                        + ", Computer's score: " + computerScore;
        scoreTextView.setText(score);
    }

    // Sets turn string
    private void setTurnString () {

        // Initialize
        String turnString = "";
        int turnScore = 0;

        // User's turn
        if ((turn % 2) == 0) {
            turnString = "Your";
            turnScore = userTurnScore;
        }
        // Computer's turn
        else {
            turnString = "Computer's";
            turnScore = computerTurnScore;
        }

        // Set string
        turnString += " turn, score: " + turnScore;
        turnTextView.setText(turnString);
    }
}
