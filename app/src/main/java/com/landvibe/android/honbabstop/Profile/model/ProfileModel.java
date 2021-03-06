package com.landvibe.android.honbabstop.profile.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;

/**
 * Created by user on 2017-02-13.
 */

public class ProfileModel {

    private final static String TAG ="ProfileModel";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private UserDataChange mUserDataChange;

    public interface UserDataChange{
        void update(User user);
    }
    public void setOnChangeListener(UserDataChange userDataChange){
        mUserDataChange=userDataChange;
    }

    public ProfileModel(){
        loadAuth();
        loadDB();
    }
    /**
     * 인증 정보 로드
     */
    private void loadAuth(){
        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void loadUser(){
        if(mDatabase==null || mUser==null){
            return;
        }

        String uid = mUser.getUid();
        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                User user = dataSnapshot.getValue(User.class);
                UserStore.getInstance().saveUser(user);
                if(mUserDataChange!=null){
                    mUserDataChange.update(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
            }
        });
    }

    public void saveUser(User user){
        if(mDatabase==null || mUser==null){
            return;
        }
        UserStore.getInstance().saveUser(user);
        String uid = mUser.getUid();
        mDatabase.child("users").child(uid).setValue(user);
    }

    public void notifyUserInfoChange(){
        User user = UserStore.getInstance().getUser();
        if (user==null){
            return;
        }
        mUserDataChange.update(user);
    }



}
