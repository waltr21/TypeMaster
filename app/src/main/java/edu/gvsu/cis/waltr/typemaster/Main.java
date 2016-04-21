package edu.gvsu.cis.waltr.typemaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

public class Main extends AppCompatActivity {
    private CardView minuteCard, wordCard, practiceCard, scoreCard;
    private boolean wordGame, minuteGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordGame = false;
        minuteGame = false;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);


        /* If the user is not connected to the internet then they are given
        a warning and booted out of the app */
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(Main.this)
                    .setTitle("Connect to the internet")
                    .setMessage("We have detected that you are not connected to the internet." +
                            "This app requires a stable internet connection at all times. Please come " +
                            "back when you are connected!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            finish();
                        }
                    })
                    .show();
        }


        minuteCard = (CardView) findViewById(R.id.minute_card);
        wordCard = (CardView) findViewById(R.id.word_card);
        practiceCard = (CardView) findViewById(R.id.practice_card);
        scoreCard = (CardView) findViewById(R.id.score_card);

        scoreCard.setEnabled(false);


        minuteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                //simple if statement to check which activity to go to
                minuteGame = true;
                wordGame = false;
                Intent launchMinute = new Intent(Main.this, MinuteActivity.class);
                launchMinute.putExtra("minuteGame", minuteGame);
                launchMinute.putExtra("wordGame", wordGame);
                startActivity(launchMinute);
            }
        });

        wordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                //simple if statement to check which activity to go to
                wordGame = true;
                minuteGame = false;
                Intent launchPop = new Intent(Main.this, PopUp.class);
                launchPop.putExtra("minuteGame", minuteGame);
                launchPop.putExtra("wordGame", wordGame);
                startActivity(launchPop);
            }
        });

        practiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                //simple if statement to check which activity to go to
                Intent launchPractice = new Intent(Main.this, PracticeActivity.class);
                startActivity(launchPractice);
            }
        });

        scoreCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                //simple if statement to check which activity to go to
                Intent launchScore = new Intent(Main.this, ScoreActivity.class);
                startActivity(launchScore);
            }
        });

    }

    //Is the user connected to the internet?
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
