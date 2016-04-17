package edu.gvsu.cis.waltr.typemaster;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by hunterhubers on 4/17/16.
 */
public interface API2 {
        @GET("/randomWords")
        void getWordAsync (@Query("hasDictionaryDef") boolean f, @Query("minCorpusCount") int n,
                           @Query("maxCorpusCount") int a, @Query("minDictionaryCount") int x,
                           @Query("maxDictionaryCount") int y, @Query("minLength") int z, @Query("maxLength") int q,
                           @Query("limit") int s, @Query("api_key") String zz, PracticeActivity cb);
    }


