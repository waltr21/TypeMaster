package edu.gvsu.cis.waltr.typemaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.games.Games;

public class ScoreActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

       /*StartActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
               CgkI7ryyz50REAIQAQ), REQUEST_LEADERBOARD);*/
    }
}
