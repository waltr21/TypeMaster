package edu.gvsu.cis.waltr.typemaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RyanWalt on 4/19/16.
 */
public class PopUp extends Activity {
    private Spinner spinner;
    private int wordAmount;
    private Button goButton;
    private boolean wordGame;
    private boolean minuteGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        spinner = (Spinner) findViewById(R.id.spinner);
        goButton = (Button) findViewById(R.id.goButton);

        Intent pull = getIntent();
        wordGame = pull.getBooleanExtra("wordGame", false);
        minuteGame = pull.getBooleanExtra("minuteGame", false);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .2));

        ArrayAdapter<Integer> adapter;
        List<Integer> list;

        list = new ArrayList<Integer>();
        list.add(5);
        list.add(10);
        list.add(15);
        list.add(20);
        list.add(15);
        list.add(30);
        list.add(35);
        list.add(40);
        list.add(45);
        list.add(50);

        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View press) {
                wordAmount = Integer.parseInt(spinner.getSelectedItem().toString());

                Intent launchWord = new Intent(PopUp.this, MinuteActivity.class);
                launchWord.putExtra("wordAmount", wordAmount);
                launchWord.putExtra("minuteGame", minuteGame);
                launchWord.putExtra("wordGame", wordGame);
                startActivity(launchWord);
                finish();
            }
        });
    }
}
