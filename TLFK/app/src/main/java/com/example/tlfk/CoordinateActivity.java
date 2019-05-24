package com.example.tlfk;

import android.Manifest;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.utils.LocationState;
import pub.devrel.easypermissions.EasyPermissions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoordinateActivity {


    public void dataRequest(int x, int y, int di, int time){
        Log.d("aaa","a");
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

                    Log.d("msg","success");
                }

                else {
                    Log.d("msg","error1");
                }
            }

            @Override
            public void onFailure(Call<LightInfoResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("msg","error2");
            }
        });

    }
}
