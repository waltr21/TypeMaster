package edu.gvsu.cis.waltr.typemaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        /*startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                LEADERBOARD_ID), CgkI7ryyz50REAIQAQ);*/
    }
}
