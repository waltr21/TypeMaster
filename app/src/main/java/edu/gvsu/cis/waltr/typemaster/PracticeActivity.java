package edu.gvsu.cis.waltr.typemaster;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
    private String wordP;
    private EditText userInputP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        randomWordP = (TextView) findViewById(R.id.randomWord);
        userInputP = (EditText) findViewById(R.id.editText);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.wordnik.com:80/v4/words.json")
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        API2 service = restAdapter.create(API2.class);

        service.getWordAsync(false, 0, -1, 1, -1, 3, 7, 1,
                "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5", this);


        wordP = CallBack.sendWord();
        randomWordP.setText(wordP.toUpperCase());
    }

    @Override
    public void success(List<Word> words, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
