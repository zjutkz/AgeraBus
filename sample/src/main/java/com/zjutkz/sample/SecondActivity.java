package com.zjutkz.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zjutkz.lib.AgeraBus;
import com.zjutkz.lib.listener.OnEventReceiveListener;
import com.zjutkz.sample.event.BackgroundStrEvent;

/**
 * Created by kangzhe on 16/9/7.
 */
public class SecondActivity extends AppCompatActivity implements OnEventReceiveListener{

    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        AgeraBus.eventRepositories().registerInBackgroundThread(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgeraBus.eventRepositories().unRegister(this);
    }

    public void background(View view){
        AgeraBus.eventRepositories().post(new BackgroundStrEvent("This is second activity bus"));
    }

    @Override
    public void onEventReceiveInBackground() {
        if(AgeraBus.eventRepositories().get() instanceof BackgroundStrEvent){
            try {
                Thread.sleep(2000);
                BackgroundStrEvent event = (BackgroundStrEvent) AgeraBus.eventRepositories().get();
                Log.d(TAG, "second update in background thread: " + event.getVar() + " " + Thread.currentThread());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEventReceiveInMain() {
        BackgroundStrEvent event = (BackgroundStrEvent) AgeraBus.eventRepositories().get();
        Log.d(TAG, "second update in main thread: " + event.getVar() + " " + Thread.currentThread());
    }
}
