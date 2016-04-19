package edu.gvsu.cis.waltr.typemaster;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class PracticeActivity extends AppCompatActivity {
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
    }
}
