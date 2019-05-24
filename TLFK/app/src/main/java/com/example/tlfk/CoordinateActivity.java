package com.example.tlfk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoordinateActivity {


    public void dataRequest(int x, int y, int di, int time){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<LightInfoResponse> call = retrofitService.LightInfo(x,y,di,time);
        call.enqueue(new Callback<LightInfoResponse>() {
            @Override
            public void onResponse(Call<LightInfoResponse> call, Response<LightInfoResponse> response) {
                if (response.isSuccessful()) {

                }

                else {

                }
            }

            @Override
            public void onFailure(Call<LightInfoResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
