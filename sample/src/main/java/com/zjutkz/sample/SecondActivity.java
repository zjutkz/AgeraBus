package com.zjutkz.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.agera.Updatable;
import com.zjutkz.lib.AgeraBus;
import com.zjutkz.sample.event.SampleStrEvent;

/**
 * Created by kangzhe on 16/9/7.
 */
public class SecondActivity extends AppCompatActivity implements Updatable {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        AgeraBus.eventRepositories().addUpdatableInBackground(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgeraBus.eventRepositories().removeUpdatable(this);
    }

    public void bus_2_2(View view){
        AgeraBus.eventRepositories().post(new SampleStrEvent("This is second activity bus"));
    }

    @Override
    public void update() {
        if(AgeraBus.eventRepositories().get() instanceof SampleStrEvent){
            SampleStrEvent event = (SampleStrEvent) AgeraBus.eventRepositories().get();
            Log.d("TAG", "second update: " + event.getVar() + " " + Thread.currentThread());
        }
    }
}
