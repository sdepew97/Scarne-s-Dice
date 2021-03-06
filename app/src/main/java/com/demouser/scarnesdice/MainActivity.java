package com.demouser.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView scoreText;
    private ImageView defaultImage;
    private Button rollButton;
    private Button holdButton;
    private Button resetButton;

    private int NUM_SIDES_DICE = 6;
    private int MAX_NUM_COMPUTER_TURNS = 4;
    private int WINNING_SCORE = 20;
    private int DELAY = 1000;
    private int COMPUTER_ROLLED_ONE = 0;
    private int COMPUTER_HOLDS = 1;

    //Global variables
    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int computerOverallScore = 0;
    private int computerTurnScore = 0;
    private int numRolls = 0;

    //Images for dice
    private Integer[] dicePictures = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    //Messages for computer rolling
    private String[] messages = {" Computer rolled a one", " Computer holds"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreText = findViewById(R.id.scoreTitle);
        defaultImage = findViewById(R.id.defaultDiceView);
        rollButton = findViewById(R.id.rollButton);
        holdButton = findViewById(R.id.holdButton);
        resetButton = findViewById(R.id.resetButton);

        //Click handler for "Roll" button
        rollButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                if (checkForWinner())
                {
                    //Done
                }
                else
                {
                    scoreText.setText(updateUserTitle(userOverallScore, computerOverallScore, userTurnScore));
                    int rollValue = rollDice();

                    //set the user turn score value appropriately
                    if (rollValue == 1) {
                        userTurnScore = 0;
                        scoreText.setText(updateUserTitle(userOverallScore, computerOverallScore, userTurnScore));
                        computerTurn();
                    } else {
                        userTurnScore += rollValue;
                        scoreText.setText(updateUserTitle(userOverallScore, computerOverallScore, userTurnScore));
                    }
                }
            }
        });

        //Handler for hold button
        holdButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                hold();
                computerTurn();
            }
        });

        //Handler for reset button
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
               reset();
            }
        });
    }

    private String updateUserTitle(int userScore, int computerScore, int currentValue)
    {
        return "Your score: " + userScore + " Computer score: " + computerScore + " Your score: " + currentValue;
    }

    private String updateComputerTitle(int userScore, int computerScore, int currentValue)
    {
        return "Your score: " + userScore + " Computer score: " + computerScore + " Computer's score: " + currentValue;
    }

    private String updateComputerMove(int userScore, int computerScore, String message)
    {
        return "Your score: " + userScore + " Computer score: " + computerScore + message;
    }

    private int rollDice()
    {
        Random random = new Random();
        Integer randomInt = random.nextInt(NUM_SIDES_DICE);
        defaultImage.setImageResource(dicePictures[randomInt]);

        return (randomInt + 1);
    }

    private void hold()
    {
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        scoreText.setText(updateUserTitle(userOverallScore, computerOverallScore, userTurnScore));
    }

    private void reset()
    {
        //Ensure buttons are enabled
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
        resetButton.setEnabled(true);

        userOverallScore = 0;
        userTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;
        scoreText.setText(updateUserTitle(userOverallScore, computerOverallScore, userTurnScore));
    }

    private void computerTurn() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Before the computer goes, check if a winner must be announced
                if(checkForWinner())
                {
                    //Done
                }
                else
                {
                    rollButton.setEnabled(false);
                    holdButton.setEnabled(false);

                    if(numRolls < MAX_NUM_COMPUTER_TURNS) {
                        //Roll dice...
                        int rollValue = rollDice();
                        numRolls++;

                        if (rollValue == 1) {
                            //Computer rolled a 1
                            computerTurnScore = 0;
                            scoreText.setText(updateComputerTitle(userOverallScore, computerOverallScore, computerTurnScore));
                            computerDone(COMPUTER_ROLLED_ONE);
                        }
                        else
                        {
                            //Computer did not roll a one...
                            computerTurnScore += rollValue;
                            scoreText.setText(updateComputerTitle(userOverallScore, computerOverallScore, computerTurnScore));
                            computerTurn();
                        }
                    }
                    else
                    {
                        computerDone(COMPUTER_HOLDS);
                    }
                }
            }
        }, DELAY);
    }

    private void computerDone(int index)
    {
        numRolls = 0;
        computerOverallScore += computerTurnScore;
        computerTurnScore = 0;

        scoreText.setText(updateComputerMove(userOverallScore, computerOverallScore, messages[index]));

        //Re-enable buttons
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);

        //Before the computer ends, check if a winner must be announced
        checkForWinner();
    }

    private boolean checkForWinner()
    {
        if (userOverallScore >= WINNING_SCORE || computerOverallScore >= WINNING_SCORE)
        {
            announceWinner();
            return true;
        }
        else
        {
            return false;
        }
    }

    private void announceWinner() {
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);
        resetButton.setEnabled(true);

        if(userOverallScore>=WINNING_SCORE && computerOverallScore>=WINNING_SCORE)
        {
            if(userOverallScore>computerOverallScore)
            {
                scoreText.setText("The user won!!");
            }
            else
            {
                scoreText.setText("The computer won!!");
            }
        }
        else
        {
            if (userOverallScore >= WINNING_SCORE)
            {
                scoreText.setText("The user won!!");
            }
            else
            {
                scoreText.setText("The computer won!!");
            }
        }
    }
}
