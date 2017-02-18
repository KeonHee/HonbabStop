package com.landvibe.android.honbabstop.base.domain;

/**
 * Created by user on 2017-02-13.
 */

public class UserStore {

    private static UserStore instance=null;

    private static User userInstance;

    private UserStore() {}

    public void saveUser(User user){
        userInstance=user;
    }

    public static UserStore getInstance(){
        if(instance==null){
            instance=new UserStore();
        }
        return instance;
    }

    public User getUser(){
        return userInstance!=null? userInstance:null;
    }

}
