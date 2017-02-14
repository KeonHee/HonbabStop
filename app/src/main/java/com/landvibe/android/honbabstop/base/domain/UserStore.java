package com.landvibe.android.honbabstop.base.domain;

/**
 * Created by user on 2017-02-13.
 */

public class UserStore {

    private static UserStore instance=null;

    private static User userInstance=null;

    private UserStore() {}

    public static UserStore saveUser(User user){
        if(instance==null){
            instance=new UserStore();
            userInstance=user;
        }
        return instance;
    }

    public static User getUser(){
        return userInstance!=null? userInstance:null;
    }

}
