package com.landvibe.android.honbabstop.main.model;

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

public class MainModel {

    private final static String TAG = "MainModel";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    /**
     * 초기화
     */
    public MainModel(){
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
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                User user = dataSnapshot.getValue(User.class);
                UserStore.getInstance().saveUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
            }
        });
    }

}
