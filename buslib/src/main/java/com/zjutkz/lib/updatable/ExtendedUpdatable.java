package com.zjutkz.lib.updatable;

import com.google.android.agera.Updatable;
import com.zjutkz.lib.repository.EventRepo;

import java.util.concurrent.Executor;

/**
 * Created by kangzhe on 16/9/8.
 */
public interface ExtendedUpdatable extends Updatable{

    void addedIntoRepo(final EventRepo eventRepo, Executor executor);

    void removedFromRepo(EventRepo eventRepo);
}
