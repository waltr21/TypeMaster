package edu.gvsu.cis.waltr.typemaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;




public class Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Freezable<Leaderboard>, View.OnClickListener {



    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private static int REQUEST_LEADERBOARD = 123;
    private CardView minuteCard, wordCard, practiceCard, scoreCard;
    private SignInButton signIn;
    private Button signOut;
    private boolean wordGame, minuteGame;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    private final int scoreActivityThing = 10;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(mResolvingConnectionFailure){
                mResolvingConnectionFailure = false;
                mGoogleApiClient.connect();
            }
        } else if(requestCode == scoreActivityThing){
            if(data != null) {
                double wordPerMin = data.getDoubleExtra("passingLeaderboard", 0.0);
                Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.LEADERBOARD_ID), (long) wordPerMin);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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


        //Intent pull = getIntent();
        //wordPerMinute = pull.getDoubleExtra("wordPerMinute", 0.0);

        minuteCard = (CardView) findViewById(R.id.minute_card);
        wordCard = (CardView) findViewById(R.id.word_card);
        practiceCard = (CardView) findViewById(R.id.practice_card);
        scoreCard = (CardView) findViewById(R.id.score_card);

        signIn = (SignInButton) findViewById(R.id.button2);
        signOut = (Button) findViewById(R.id.button3);

        scoreCard.setEnabled(false);

        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
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
                startActivityForResult(launchMinute, scoreActivityThing);
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

                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkI7ryyz50REAIQAQ"), REQUEST_LEADERBOARD);

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                mSignInClicked = true;
                mGoogleApiClient.connect();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                scoreCard.setEnabled(false);
                mSignInClicked = false;
                if(mGoogleApiClient.isConnected()){
                    Games.signOut(mGoogleApiClient);
                }
                mGoogleApiClient.disconnect();

                //Show and Hide sign in and out buttons.

                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        scoreCard.setEnabled(true);
        signIn.setVisibility(View.GONE);
        signOut.setVisibility(View.VISIBLE);
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
        if (mSignInClicked) {
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            if(connectionResult.hasResolution()){
                try{
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mResolvingConnectionFailure = true;
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    mResolvingConnectionFailure = false;
                    mGoogleApiClient.connect();
                }
            } else {
                Snackbar.make(signOut, connectionResult.getErrorMessage(), Snackbar.LENGTH_LONG).show();
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
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
