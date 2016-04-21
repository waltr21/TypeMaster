package edu.gvsu.cis.waltr.typemaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.games.leaderboard.Leaderboard;



public class Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Freezable<Leaderboard>, View.OnClickListener {

    private CardView minuteCard;
    private CardView wordCard;
    private CardView practiceCard;
    private CardView scoreCard;

    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;



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


        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();


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




      // initialized in onCreate

    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Hello","Bye");
        // show sign-out button, hide the sign-in button
        //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        //findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

        // (your code here: update UI, enable functionality that
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."\
            String errorString = getResources().getString(R.string.signin_other_error);
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult, RC_SIGN_IN, errorString)) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public Leaderboard freeze() {
        return null;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
