package com.zjutkz.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.agera.Updatable;
import com.zjutkz.lib.AgeraBus;
import com.zjutkz.sample.event.SampleStrEvent;


/**
 * Created by kangzhe on 16/9/8.
 */
public class MainActivity extends AppCompatActivity implements Updatable {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AgeraBus.eventRepositories().addUpdatable(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgeraBus.eventRepositories().removeUpdatable(this);
    }

    public void jump(View view){
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void delay_bus(View view){
        AgeraBus.eventRepositories().postDelay(new SampleStrEvent("This is a delay event"),1000);
    }

    public void bus(View view){
        AgeraBus.eventRepositories().post(new SampleStrEvent("This is an event"));
    }

    public void thread_bus(View view){
        /*try {
            AgeraBus.eventRepositories().postWithBackground(getEventAfter2s());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    private Object getEventAfter2s() throws InterruptedException {
        //Thread.sleep(2000);
        return new SampleStrEvent("This is a time-consuming event");
    }

    @Override
    public void update() {
        if(AgeraBus.eventRepositories().get() instanceof SampleStrEvent){
            SampleStrEvent event = (SampleStrEvent) AgeraBus.eventRepositories().get();
            Log.d(TAG, "update: " + event.getVar() + " " + Thread.currentThread());
        }
    }
}

