package edu.gvsu.cis.waltr.typemaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class MinuteActivity extends AppCompatActivity implements Callback<List<Word>>, View.OnKeyListener {
    private TextView randomWord;
    private String wordString;
    private EditText userInput;
    private API service;
    private Button go;
    private int numRight;
    private int numWrong;
    private int totalWords;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute);

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        randomWord = (TextView) findViewById(R.id.randomWord);
        userInput = (EditText) findViewById(R.id.editText);
        go = (Button) findViewById(R.id.button);
        userInput.setOnKeyListener(this);

        //Launches the new activity after 60 seconds
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent launchScore = new Intent(MinuteActivity.this, ScoreReview.class);
                launchScore.putExtra("numRight", numRight);
                launchScore.putExtra("numWrong", numWrong);
                launchScore.putExtra("totalWords", totalWords);
                startActivity(launchScore);
            }
        }, 60000);

        //API set up...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.wordnik.com:80/v4/words.json")
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        service = restAdapter.create(API.class);

        //Generates a random word
        generateWord();

        /*A tempoary button that fulfils the job of the enter button
        on the users keyboard. This will soon be fixed... */

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View press) {
                    //simple if statement to check which activity to go to
                    totalWords++;


                    //Takes what the user entered
                    String userWord = userInput.getText().toString();

                    //Counts num right and wrong
                    if (userWord.toLowerCase().equals(wordString.toLowerCase())) {
                        numRight++;

                    }

                    else {
                        numWrong++;
                    }

                    //Resets the text for the user...
                    userInput.setText("");

                    generateWord();
                }
            });

    }

    //Calls the service method to geet a random word
    public void generateWord(){
        service.getWordAsync(false, 0, -1, 1, -1, 3, 7, 1,
                "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5", this);
    }

    //Pulls a word from the arraylist and sets the text to randomWord
    @Override
    public void success(List<Word> words, Response response) {
            for (Word w : words) {
        wordString = w.word;
    }
    randomWord.setText(wordString.toUpperCase());
    }

    @Override
    public void failure(RetrofitError error) {}

    /*Checks if the user presses enter on the keyboard
    *this currently doesn't work and I'm not sure why. I'll have to work
    * on it later. I placed a temporary button to do the roll of what will
    * soon be the enter button. */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            generateWord();
            return true;
        }
        else{
            return false;
        }
    }


}