package edu.gvsu.cis.waltr.typemaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ScoreReview extends AppCompatActivity {
    private int numRight;
    private int numWrong;
    private int wordsPerMinute;
    private int totalWords;
    private TextView wpmText;
    private TextView numErros;
    private TextView numRightText;
    private TextView wordsSeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_review);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        wpmText = (TextView) findViewById(R.id.wpmText);
        numErros = (TextView) findViewById(R.id.numErrorsText);
        numRightText = (TextView) findViewById(R.id.numRightText);
        wordsSeen = (TextView) findViewById(R.id.numSeen);


        Intent pull = getIntent();
        numRight = pull.getIntExtra("numRight", 0);
        numWrong = pull.getIntExtra("numWrong", 0);
        totalWords = pull.getIntExtra("totalWords", 0);

        wordsPerMinute = numRight;
        if (wordsPerMinute < 0){
            wordsPerMinute = 0;
        }

        numRightText.setText(numRight + "");
        numErros.setText(numWrong + "");
        wpmText.setText(wordsPerMinute + "");
        wordsSeen.setText(totalWords + "");




    }
    //Handles the back button so the user cant go back into typing
    @Override
    public void onBackPressed() {
        Intent launchScore = new Intent(ScoreReview.this, Main.class);
        startActivity(launchScore);
    }
}
