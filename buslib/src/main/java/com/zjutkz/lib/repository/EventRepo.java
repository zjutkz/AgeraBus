package com.zjutkz.lib.repository;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.agera.ActivationHandler;
import com.google.android.agera.Observables;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;
import com.google.android.agera.UpdateDispatcher;
import com.zjutkz.lib.listener.OnEventReceiveListener;
import com.zjutkz.lib.updatable.BackgroundUpdatable;
import com.zjutkz.lib.updatable.ExtendedUpdatable;
import com.zjutkz.lib.updatable.NormalUpdatable;

import java.util.HashMap;
import java.util.concurrent.Executor;

/**
 * Created by kangzhe on 16/9/8.
 */
public final class EventRepo implements Repository<Object>, Receiver<Object>, ActivationHandler {

    private final static Object DEFAULT = new Object();

    private final UpdateDispatcher updateDispatcher;

    private Handler postHandler;

    private Object holder;

    private boolean hasUpdatable;

    private HashMap<OnEventReceiveListener,ExtendedUpdatable[]> registeredMap;

    public EventRepo() {
        this.updateDispatcher = Observables.updateDispatcher(this);

        this.holder = DEFAULT;

        postHandler = new Handler();

        registeredMap = new HashMap<>();
    }

    /**
     * register a listener which refer to activity,fragment or sth like that in main Thread
     * @param listener
     */
    public synchronized void registerInMainThread(OnEventReceiveListener listener){
        if(registeredMap.containsKey(listener)){
            throw new RuntimeException("cannot register twice!");
        }

        NormalUpdatable normalUpdatable = new NormalUpdatable(listener);
        normalUpdatable.addedIntoRepo(this,null);

        registeredMap.put(listener,new ExtendedUpdatable[]{normalUpdatable});
    }

    /**
     * register a listener which refer to activity,fragment or sth like that in background Thread
     * @param listener
     */
    public synchronized void registerInBackgroundThread(OnEventReceiveListener listener){
        registerInBackgroundThread(listener,null);
    }

    /**
     * register a listener which refer to activity,fragment or sth like that in background Thread
     * @param listener
     */
    public synchronized void registerInBackgroundThread(OnEventReceiveListener listener,Executor executor){
        if(registeredMap.containsKey(listener)){
            throw new RuntimeException("cannot register twice!");
        }

        BackgroundUpdatable backgroundUpdatable = new BackgroundUpdatable(listener);
        backgroundUpdatable.addedIntoRepo(this,executor);

        registeredMap.put(listener,new ExtendedUpdatable[]{backgroundUpdatable});
    }

    /**
     * register a listener which refer to activity,fragment or sth like that in both main and background thread
     * @param listener
     */
    public synchronized void register(OnEventReceiveListener listener){
        register(listener,null);
    }

    /**
     * register a listener which refer to activity,fragment or sth like that in both main and background thread
     * @param listener
     * @param executor
     */
    public synchronized void register(OnEventReceiveListener listener, Executor executor){
        if(registeredMap.containsKey(listener)){
            throw new RuntimeException("cannot register twice!");
        }

        NormalUpdatable normalUpdatable = new NormalUpdatable(listener);
        BackgroundUpdatable backgroundUpdatable = new BackgroundUpdatable(listener);

        normalUpdatable.addedIntoRepo(this,null);
        backgroundUpdatable.addedIntoRepo(this,executor);

        registeredMap.put(listener,new ExtendedUpdatable[]{normalUpdatable,backgroundUpdatable});
    }

    public synchronized void unRegister(OnEventReceiveListener listener) {
        if (!registeredMap.containsKey(listener)) {
            throw new RuntimeException("listener is not register!");
        }

        ExtendedUpdatable[] updatables = registeredMap.get(listener);
        for(ExtendedUpdatable updatable : updatables){
            updatable.removedFromRepo(this);
        }
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
