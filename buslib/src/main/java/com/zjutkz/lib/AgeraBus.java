package com.zjutkz.lib;

import com.zjutkz.lib.repository.EventRepo;

/**
 * Created by kangzhe on 16/9/7.
 */
public class AgeraBus {

    private static volatile EventRepo instance;

    public static EventRepo eventRepositories(){
        if(instance == null){
            synchronized (AgeraBus.class){
                if(instance == null){
                    instance = new EventRepo();
                }
            }
        }

        return instance;
    }
}
