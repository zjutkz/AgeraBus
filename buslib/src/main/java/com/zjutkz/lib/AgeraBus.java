package com.zjutkz.lib;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Receiver;
import com.google.android.agera.Supplier;

/**
 * Created by kangzhe on 16/9/7.
 */
public class AgeraBus extends BaseObservable implements Receiver<Object>,Supplier<Object> {

    private static volatile AgeraBus instance;
    private static final Object DEFAULT = new Object();

    private Object holder;

    public static AgeraBus getInstance() {
        if(instance == null){
            synchronized (AgeraBus.class){
                if(instance == null){
                    instance = new AgeraBus();
                }
            }
        }
        return instance;
    }

    public AgeraBus(){
        holder = DEFAULT;
    }

    @Override
    public void accept(@NonNull Object value) {
        synchronized (this) {
            this.holder = value;
        }

        dispatchUpdate();
    }

    @NonNull
    @Override
    public Object get() {
        return holder;
    }

    @Override
    protected void observableActivated() {
        Log.d("TAG", "observableActivated: ");
    }

    @Override
    protected void observableDeactivated() {
        Log.d("TAG", "observableDeactivated: ");
    }
}
