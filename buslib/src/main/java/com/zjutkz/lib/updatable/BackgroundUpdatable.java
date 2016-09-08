package com.zjutkz.lib.updatable;

import android.os.Looper;

import com.google.android.agera.Updatable;
import com.zjutkz.lib.listener.OnEventReceiveListener;
import com.zjutkz.lib.repository.EventRepo;
import com.zjutkz.lib.utils.Predictable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kangzhe on 16/9/8.
 */
public class BackgroundUpdatable implements Updatable{

    private OnEventReceiveListener listener;

    public BackgroundUpdatable(OnEventReceiveListener listener){
        this.listener = listener;
    }

    public void addedIntoRepo(EventRepo eventRepo){
        addedIntoRepo(eventRepo,null);
    }

    public void addedIntoRepo(final EventRepo eventRepo, Executor executor){
        if(executor == null){
            executor = Executors.newSingleThreadExecutor();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    eventRepo.addUpdatable(BackgroundUpdatable.this);
                    Looper.loop();
                }
            });
        }
    }

    public void removedFromRepo(EventRepo eventRepo){
        eventRepo.removeUpdatable(this);
    }

    @Override
    public void update() {
        Predictable.checkNull(listener,"OnEventReceiverListener cannot be null!");
        listener.onEventReceiveInBackground();
    }
}
