package com.zjutkz.lib.updatable;

import com.google.android.agera.Updatable;
import com.zjutkz.lib.listener.OnEventReceiveListener;
import com.zjutkz.lib.repository.EventRepo;
import com.zjutkz.lib.utils.Predictable;

/**
 * Created by kangzhe on 16/9/8.
 */
public class NormalUpdatable implements Updatable{

    private OnEventReceiveListener listener;

    public NormalUpdatable(OnEventReceiveListener listener){
        this.listener = listener;
    }

    public void addedIntoRepo(EventRepo eventRepo){
        eventRepo.addUpdatable(this);
    }

    public void removedFromRepo(EventRepo eventRepo){
        eventRepo.removeUpdatable(this);
    }

    @Override
    public void update() {
        Predictable.checkNull(listener,"OnEventReceiverListener cannot be null!");
        listener.onEventReceiveInMain();
    }
}
