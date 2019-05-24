package com.example.tlfk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    public static final String baseURL = "http://127.0.0.1:8000/";

    @GET("LightInfo/")
    Call<LightInfoResponse> LightInfo(
            @Query("x") int x,
            @Query("y") int y,
            @Query("di") int di,
            @Query("time") int time
    );
}
