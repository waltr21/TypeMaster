package edu.gvsu.cis.waltr.typemaster;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gtranslate.Language;
import com.gtranslate.Translator;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;



import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class MinuteActivity extends AppCompatActivity implements Callback<List<Word>> {
    private TextView randomWord;
    private String wordString;
    private EditText userInput;
    private API service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute);

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        randomWord = (TextView) findViewById(R.id.randomWord);
        userInput = (EditText) findViewById(R.id.editText);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.wordnik.com:80/v4/words.json")
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        service = restAdapter.create(API.class);

        generateWord();

        randomWord.setText(wordString);

        handleEnter();
    }

    public void handleEnter(){
        userInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        generateWord();
                        return true;
                    }
                return false;
            }
        });
    }

    public void generateWord(){
        service.getWordAsync(false, 0, -1, 1, -1, 3, 7, 1,
                "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5", this);
    }

    @Override
    public void success(List<Word> words, Response response) {
        for (Word w : words) {
            wordString = w.word;

        }
        if (getString(R.string.lang).equals("German")) {
            Translator translate = Translator.getInstance();
            wordString = translate.translate(wordString, Language.ENGLISH, Language.GERMAN);
            randomWord.setText(wordString.toUpperCase());
        } else {
            randomWord.setText(wordString.toUpperCase());
        }
    }

    @Override
    public void failure(RetrofitError error) {}
}