package com.example.tlfk;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.utils.LocationState;
import pub.devrel.easypermissions.EasyPermissions;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private WebView mWebView;
    private String myUrl = "http://cs.kookmin.ac.kr";
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    private ConstraintLayout mLayout;
    private static final int RC_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setColor(0xFFFF0000); // 테스트 컬러 레드

        // 웹뷰 셋팅
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(myUrl);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.setBackgroundColor(0xB3FFFFFF);

        // 카메라 세팅
        surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        requestLocationPermission();

        // Get location
        getUserLocation();
    }

    //상단바, 타이틀바, 배경 컬러 설정
    void setColor(int color) {
        setStatusBarColor(this, color);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        mLayout = (ConstraintLayout)findViewById(R.id.mLayout);
        mLayout.setBackgroundColor(color);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if (camera == null) {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // View 가 존재하지 않을 때
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        // 작업을 위해 잠시 멈춘다
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // 에러가 나더라도 무시한다.
        }

        // 카메라 설정을 다시 한다.
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        camera.setParameters(parameters);

        // View 를 재생성한다.
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }

    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    //Get user Longitude , Latitude
    private void getUserLocation() {
        LocationState locationState = SmartLocation.with(this).location().state();
        // Check if the location services are enabled
        if(locationState.locationServicesEnabled() && locationState.isGpsAvailable()){
            // One Fix location
            SmartLocation.with(this).location().oneFix().config(LocationParams.BEST_EFFORT).start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    Log.d("Locations lat", ""+ location.getLatitude());
                    Log.d("Locations long", ""+ location.getLongitude());

                    Toast.makeText(MainActivity.this, "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    int x = (int)location.getLatitude();
                    int y = (int)location.getLongitude();
                    CoordinateActivity coordinateActivity = new CoordinateActivity();
                    coordinateActivity.dataRequest(x/10,y/10,150,30);
                }
            });
        } else {
            // Minta askses GPS
            Toast.makeText(this, "Location Service & GPS are disabled. Please enable !", Toast.LENGTH_SHORT).show();
        }
    }
    //permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        // Get user location
        getUserLocation();
    }
    private void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            getUserLocation();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Membutuhkan akses GPS",
                    RC_LOCATION, perms);

        }
    }
}
