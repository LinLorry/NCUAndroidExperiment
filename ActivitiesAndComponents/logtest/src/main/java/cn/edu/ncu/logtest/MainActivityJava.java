package cn.edu.ncu.logtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class MainActivityJava extends Activity {

    private final static String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "OnCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "OnReStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
