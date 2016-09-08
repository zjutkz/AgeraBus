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

        AgeraBus.getInstance().addUpdatable(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgeraBus.getInstance().removeUpdatable(this);
    }

    public void jump(View view){
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void bus_2(View view){
        AgeraBus.getInstance().accept(new SampleStrEvent("This is an event_2"));
    }

    public void bus(View view){
        AgeraBus.getInstance().accept(new SampleStrEvent("This is an event"));
    }


    @Override
    public void update() {
        if(AgeraBus.getInstance().get() instanceof SampleStrEvent){
            SampleStrEvent event = (SampleStrEvent) AgeraBus.getInstance().get();
            Log.d(TAG, "update: " + event.getVar());
        }
    }
}

