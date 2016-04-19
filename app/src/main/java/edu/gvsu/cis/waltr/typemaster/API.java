package edu.gvsu.cis.waltr.typemaster;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by RyanWalt on 4/8/16.
 */
public interface API {
    @GET("/randomWords")
    void getWordAsync (@Query("hasDictionaryDef") boolean f, @Query("minCorpusCount") int n,
                       @Query("maxCorpusCount") int a, @Query("minDictionaryCount") int x,
                       @Query("maxDictionaryCount") int y, @Query("minLength") int z, @Query("maxLength") int q,
                       @Query("limit") int s, @Query("api_key") String zz, Callback<List<Word>> c);
}
