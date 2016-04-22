package edu.gvsu.cis.waltr.typemaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.example.games.basegameutils.BaseGameUtils;



public class Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Freezable<Leaderboard>, View.OnClickListener {



    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private static int REQUEST_LEADERBOARD = 9;
    private CardView minuteCard, wordCard, practiceCard, scoreCard;
    private boolean wordGame, minuteGame;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }





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
                    .setTitle(getResources().getString(R.string.connect))
                    .setMessage(getResources().getString(R.string.connect_msg))
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
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkI7ryyz50REAIQAQ"), REQUEST_LEADERBOARD);
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
        scoreCard.setEnabled(true);
        // show sign-out button, hide the sign-in button
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

    //Is the user connected to the internet?
    public boolean isNetworkAvailable(){
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
