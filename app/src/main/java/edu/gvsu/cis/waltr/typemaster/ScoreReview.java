package edu.gvsu.cis.waltr.typemaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ScoreReview extends AppCompatActivity {
    private int numRight, numWrong, totalWords;
    private double wordsPerMinute, time;
    private TextView wpmText, numErrors, numRightText, wordsSeen, timeText;
    private boolean wordGame, minuteGame;
    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_review);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        wpmText = (TextView) findViewById(R.id.wpmText);
        numErrors = (TextView) findViewById(R.id.numErrorsText);
        numRightText = (TextView) findViewById(R.id.numRightText);
        wordsSeen = (TextView) findViewById(R.id.numSeen);
        timeText = (TextView) findViewById(R.id.timeText);
        home = (Button) findViewById(R.id.button);

        Intent pull = getIntent();
        numRight = pull.getIntExtra("numRight", 0);
        numWrong = pull.getIntExtra("numWrong", 0);
        totalWords = pull.getIntExtra("totalWords", 0);
        wordGame = pull.getBooleanExtra("wordGame", false);
        minuteGame = pull.getBooleanExtra("minuteGame", false);
        time = pull.getDoubleExtra("time", 0.0);

        if (minuteGame)
        wordsPerMinute = numRight;

        if (wordGame){
            wordsPerMinute = (60 / time) * numRight;

            timeText.setText(df.format(time) + " seconds");
        }
        if (wordsPerMinute < 0){
            wordsPerMinute = 0;
        }

        numRightText.setText(numRight + "");
        numErrors.setText(numWrong + "");
        wpmText.setText(df.format(wordsPerMinute) + "");
        wordsSeen.setText(totalWords + "");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                Intent passLeaderboard = new Intent();
                passLeaderboard.putExtra("passingLeaderboard", wordsPerMinute);
                setResult(RESULT_OK, passLeaderboard);
                finish();
            }
        });
    }
    //Handles the back button so the user cant go back into typing
    @Override
    public void onBackPressed() {
        Intent launchScore = new Intent(ScoreReview.this, Main.class);
        launchScore.putExtra("wordsPerMinute", wordsPerMinute);
        startActivity(launchScore);
    }
}
