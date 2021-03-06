package com.zjutkz.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zjutkz.lib.AgeraBus;
import com.zjutkz.lib.listener.OnEventReceiveListener;
import com.zjutkz.sample.event.SampleStrEvent;


/**
 * Created by kangzhe on 16/9/8.
 */
public class MainActivity extends AppCompatActivity implements OnEventReceiveListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AgeraBus.eventRepositories().registerInMainThread(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgeraBus.eventRepositories().unRegister(this);
    }

    public void jump(View view){
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void delay_bus(View view){
        AgeraBus.eventRepositories().postDelay(new SampleStrEvent("This is a delay event"),1000);
    }

    public void bus(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AgeraBus.eventRepositories().post(new SampleStrEvent("This is an event"));
            }
        }).start();
    }

    @Override
    public void onEventReceiveInMain() {
        if(AgeraBus.eventRepositories().get() instanceof SampleStrEvent){
            SampleStrEvent event = (SampleStrEvent) AgeraBus.eventRepositories().get();
            Log.d(TAG, "update in main thread: " + event.getVar() + " " + Thread.currentThread());
        }
    }

    @Override
    public void onEventReceiveInBackground() {
        if(AgeraBus.eventRepositories().get() instanceof SampleStrEvent){
            SampleStrEvent event = (SampleStrEvent) AgeraBus.eventRepositories().get();
            Log.d(TAG, "update in background thread: " + event.getVar() + " " + Thread.currentThread());
        }
    }
}

