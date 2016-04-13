package edu.gvsu.cis.waltr.typemaster;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by RyanWalt on 4/11/16.
 */
public class CallBack implements Callback<List<Word>> {
    public static String word;

        @Override
        public void success (List <Word> words, Response response){
            for (Word w : words) {
                word = w.word;
            }
        }

    @Override
    public void failure(RetrofitError error) {
    }

    public static String sendWord(){
        if (word == null){
            word = "hey";
        }
        return word;
    }
}
