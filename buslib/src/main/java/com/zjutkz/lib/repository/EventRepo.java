package com.zjutkz.lib.repository;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.agera.ActivationHandler;
import com.google.android.agera.Observables;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;
import com.google.android.agera.UpdateDispatcher;

/**
 * Created by kangzhe on 16/9/8.
 */
public final class EventRepo implements Repository<Object>, Receiver<Object>, ActivationHandler {

    private final static Object DEFAULT = new Object();

    private final UpdateDispatcher updateDispatcher;

    private Handler postHandler;

    private Object holder;

    private boolean hasUpdatable;

    public EventRepo() {
        this.updateDispatcher = Observables.updateDispatcher(this);

        this.holder = DEFAULT;

        postHandler = new Handler();
    }


    @NonNull
    @Override
    public synchronized Object get() {
        return holder;
    }

    /**
     * post an event which you want to receive somewhere
     * @param event
     */
    public void post(@NonNull Object event){
        postDelay(event,0);
    }


    /**
     * post an event [delay] ms later
     * @param event
     * @param delay
     */
    public void postDelay(@NonNull final Object event, long delay){
        postHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                accept(event);
            }
        },delay);
    }

    public boolean hasUpdatable(){
        return this.hasUpdatable;
    }

    @Override
    public void accept(@NonNull final Object holder) {
        synchronized (this) {
            this.holder = holder;
        }
        updateDispatcher.update();
    }

    @Override 
    public void addUpdatable(@NonNull final Updatable updatable) {
        updateDispatcher.addUpdatable(updatable);
    }


    @Override 
    public void removeUpdatable(@NonNull final Updatable updatable) {
        updateDispatcher.removeUpdatable(updatable);
    }

    @Override
    public void observableActivated(@NonNull UpdateDispatcher caller) {
        hasUpdatable = true;
    }

    @Override
    public void observableDeactivated(@NonNull UpdateDispatcher caller) {
        hasUpdatable = false;
    }
}
