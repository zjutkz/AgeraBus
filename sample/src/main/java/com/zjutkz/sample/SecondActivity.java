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
        AgeraBus.getInstance().addUpdatable(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgeraBus.getInstance().removeUpdatable(this);
    }

    public void bus_2_2(View view){
        AgeraBus.getInstance().accept(new SampleStrEvent("This is second activity bus"));
    }

    @Override
    public void update() {
        if(AgeraBus.getInstance().get() instanceof SampleStrEvent){
            SampleStrEvent event = (SampleStrEvent) AgeraBus.getInstance().get();
            Log.d("TAG", "second update: " + event.getVar());
        }
    }
}
