package com.zjutkz.lib.utils;

/**
 * Created by kangzhe on 16/9/8.
 */
public class Predictable {

    public static boolean checkNull(Object checked,String desc){
        if(checked != null){
            return true;
        }else {
            throw new NullPointerException(desc);
        }
    }
}
