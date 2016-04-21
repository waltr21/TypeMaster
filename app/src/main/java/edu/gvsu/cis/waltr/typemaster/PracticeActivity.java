package edu.gvsu.cis.waltr.typemaster;

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
    private TextView randomWordP;
    private EditText userInputP;
    private API service;
    private String wordString;
    private int numWrong, numRight;

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

        randomWordP.setText("");

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
        service.getWordAsync(true, 0, -1, 1, -1, 3, 7, 1,
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
        //Counts num right and wrong
        if (userWord.toLowerCase().equals(wordString.toLowerCase() + " "))
            numRight++;
        else
        numWrong++;
        //Resets the text for the user...
        userInputP.setText("");
        generateWord();
    }
}
