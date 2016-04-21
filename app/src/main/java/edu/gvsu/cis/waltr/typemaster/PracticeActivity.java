package edu.gvsu.cis.waltr.typemaster;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class PracticeActivity extends AppCompatActivity implements Callback<List<Word>> {
    private TextView randomWordP, prevWordView;
    private EditText userInputP, minInput, maxInput;
    private API service;
    private String wordString, prevWordString;
    private int minLength, maxLength;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.wordnik.com:80/v4/words.json")
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        service = restAdapter.create(API.class);

        randomWordP = (TextView) findViewById(R.id.randomWordP);
        userInputP = (EditText) findViewById(R.id.userInputP);
        minInput = (EditText) findViewById(R.id.minLength);
        maxInput = (EditText) findViewById(R.id.maxLength);
        prevWordView = (TextView) findViewById(R.id.prevWordPractice);

        randomWordP.setText("");
        prevWordView.setText("");

        minLength = 3;
        maxLength = 7;

        minInput.setText("3");
        maxInput.setText("7");

        generateWord();

        spaceCheck();
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
        randomWordP.setText(wordString.toUpperCase());
    }

    @Override
    public void failure(RetrofitError error) {
    }

    public void generateWord() {
        service.getWordAsync(true, 0, -1, 1, -1, minLength, maxLength, 1,
                "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5", this);
    }

    public void spaceCheck(){
        userInputP.addTextChangedListener(new TextWatcher() {
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

    public void compare() {
        //Takes what the user entered
        String userWord = userInputP.getText().toString();
        prevWordString = userWord;

        if (minInput.length() > 0 && maxInput.length() > 0){
            minLength = Integer.parseInt(minInput.getText().toString());
            maxLength = Integer.parseInt(maxInput.getText().toString());
            if (minLength > maxLength){
                int minTemp = minLength;
                minLength = maxLength;
                maxLength = minTemp;
                minInput.setText(minLength + "");
                maxInput.setText(maxLength + "");
            }
        }

        //Counts num right and wrong
        if (userWord.toLowerCase().equals(wordString.toLowerCase() + " ")) {
            prevWordView.setText(prevWordString);
            prevWordView.setTextColor(Color.parseColor("#1f7a1f"));
        }
        else{
            prevWordView.setText(prevWordString);
            prevWordView.setTextColor(Color.RED);
        }
        //Resets the text for the user...
        userInputP.setText("");
        generateWord();
    }
}
