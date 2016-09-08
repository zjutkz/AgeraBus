package com.zjutkz.lib.updatable;

import com.zjutkz.lib.listener.OnEventReceiveListener;
import com.zjutkz.lib.repository.EventRepo;
import com.zjutkz.lib.utils.Predictable;

import java.util.concurrent.Executor;

/**
 * Created by kangzhe on 16/9/8.
 */
public class NormalUpdatable implements ExtendedUpdatable{

    private OnEventReceiveListener listener;

    public NormalUpdatable(OnEventReceiveListener listener){
        this.listener = listener;
    }

    @Override
    public void addedIntoRepo(EventRepo eventRepo, Executor executor) {
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
