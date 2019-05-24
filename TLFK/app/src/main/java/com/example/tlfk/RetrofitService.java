package com.example.tlfk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    public static final String baseURL = "http://127.0.0.1:8000/";

    @GET("login/")
    Call<LoginResponse> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("forecast/")
    Call<ForecastResponse> forecast(
            @Query("item") int item
    );

    @GET("pqmp/")
    Call<PQMPResponse> pqmp(
            @Query("item") int item
    );
}
