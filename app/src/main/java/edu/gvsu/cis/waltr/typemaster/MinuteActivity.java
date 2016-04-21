package edu.gvsu.cis.waltr.typemaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class MinuteActivity extends AppCompatActivity implements Callback<List<Word>> {
    private TextView randomWord, counter, prevWordView;
    private String wordString, prevWordString;
    private EditText userInput;
    private API service;
    private int numRight, numWrong, totalWords, wordLimit;
    private Timer timer;
    private boolean wordGame, minuteGame, stopped, alertOpen;
    public double startTime, endTime, elapsedMilliSeconds, elapsedSeconds;
    CountDownTimer cT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        //Opens the instructions on launch.
        alert();
        alertOpen =  true;

        Intent pull = getIntent();
        wordGame = pull.getBooleanExtra("wordGame", false);
        minuteGame = pull.getBooleanExtra("minuteGame", false);
        wordLimit = pull.getIntExtra("wordAmount", 0);

        stopped = false;

        if (wordGame)
            startTime = SystemClock.elapsedRealtime();

        randomWord = (TextView) findViewById(R.id.randomWordP);
        userInput = (EditText) findViewById(R.id.editText);
        counter = (TextView) findViewById(R.id.counter);
        prevWordView= (TextView) findViewById(R.id.prevWord);

        prevWordView.setText("");
        randomWord.setText("");

        //API set up...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.wordnik.com:80/v4/words.json")
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        service = restAdapter.create(API.class);

        //Generates a random word
        generateWord();

        spaceCheck();
    }

    //Calls the service method to geet a random word
    public void generateWord() {
        service.getWordAsync(true, 0, -1, 1, -1, 3, 7, 1,
                "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5", this);
    }

    //Pulls a word from the arraylist and sets the text to randomWord
    @Override
    public void success(List<Word> words, Response response) {
        for (Word w : words) {
            wordString = w.word;
        }
        //avoids a weird word with a space in it...
        while (wordString.contains(" ")){
            generateWord();
        }
        randomWord.setText(wordString.toUpperCase());

    }

    @Override
    public void failure(RetrofitError error) {
    }

    @Override
    public void onBackPressed() {
        if (!alertOpen)
        timer.cancel();
        Intent launchMain = new Intent(MinuteActivity.this, Main.class);
        startActivity(launchMain);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (minuteGame && !alertOpen) {
            timer.cancel();
        }
        stopped = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stopped) {
            Intent launchMain = new Intent(MinuteActivity.this, Main.class);
            startActivity(launchMain);
        }
    }

    public void compare() {

        //simple if statement to check which activity to go to
        totalWords++;
        //Sets the text for word game only
        if (wordGame)
            counter.setText(totalWords + "");
        //Takes what the user entered
        String userWord = userInput.getText().toString();
        prevWordString = userWord;
        //Counts num right and wrong
        if (userWord.toLowerCase().equals(wordString.toLowerCase() + " ")){
            numRight++;
            prevWordView.setText(prevWordString);
            prevWordView.setTextColor(Color.parseColor("#1f7a1f"));
        }
        else {
            numWrong++;
            prevWordView.setText(prevWordString);
            prevWordView.setTextColor(Color.RED);
        }
        //Resets the text for the user...
        userInput.setText("");

        if (wordGame) {
            if (wordLimit == totalWords) {
                endTime = SystemClock.elapsedRealtime();
                elapsedMilliSeconds = endTime - startTime;
                elapsedSeconds = elapsedMilliSeconds / 1000.0;
                Intent launchScore = new Intent(MinuteActivity.this, ScoreReview.class);
                launchScore.putExtra("numRight", numRight);
                launchScore.putExtra("numWrong", numWrong);
                launchScore.putExtra("totalWords", totalWords);
                launchScore.putExtra("minuteGame", minuteGame);
                launchScore.putExtra("wordGame", wordGame);
                launchScore.putExtra("time", elapsedSeconds);
                startActivity(launchScore);
            }
        }
        generateWord();
    }

    public void alert() {
        new AlertDialog.Builder(MinuteActivity.this)
                .setTitle(getResources().getString(R.string.how_to))
                .setMessage(getResources().getString(R.string.how_to_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertOpen = false;
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        startTime();

                    }
                })
                .show();
    }

    public void spaceCheck(){
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    char last = s.charAt(s.length() - 1);
                    if (' ' == last) {
                        compare();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void startTime(){
        //Starts the timer of the activity for the minute game.
        if (minuteGame && !alertOpen) {
            cT = new CountDownTimer(61000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String v = String.format("%02d", millisUntilFinished / 60000);
                    int va = (int) ((millisUntilFinished % 60000) / 1000);
                    counter.setText(v + ":" + String.format("%02d", va));
                }

                public void onFinish() {
                    counter.setText("Done!");
                }
            };
            cT.start();

            //Launches the new activity after 60 seconds
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent launchScore = new Intent(MinuteActivity.this, ScoreReview.class);
                    launchScore.putExtra("numRight", numRight);
                    launchScore.putExtra("numWrong", numWrong);
                    launchScore.putExtra("totalWords", totalWords);
                    launchScore.putExtra("minuteGame", minuteGame);
                    launchScore.putExtra("wordGame", wordGame);
                    startActivity(launchScore);
                }
            }, 60000);
        }
    }
}