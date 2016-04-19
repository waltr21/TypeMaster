package edu.gvsu.cis.waltr.typemaster;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main extends AppCompatActivity {
    private CardView minuteCard;
    private CardView wordCard;
    private CardView practiceCard;
    private CardView scoreCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        minuteCard = (CardView) findViewById(R.id.minute_card);
        wordCard = (CardView) findViewById(R.id.word_card);
        practiceCard = (CardView) findViewById(R.id.practice_card);
        scoreCard = (CardView) findViewById(R.id.score_card);


//thing two esfasdkjsdnv
        minuteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                //simple if statement to check which activity to go to
                Intent launchMinute = new Intent(Main.this, MinuteActivity.class);
                startActivity(launchMinute);
            }
        });

        wordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                //simple if statement to check which activity to go to
                Intent launchWord = new Intent(Main.this, WordActivity.class);
                startActivity(launchWord);
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
}
