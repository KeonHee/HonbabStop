package com.landvibe.android.honbabstop.auth.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by user on 2017-02-10.
 */

public class FirebaseAuthModel {
    private final static String TAG = "FirebaseAuthModel";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseAuthCallback firebaseAuthCallback;

    public interface FirebaseAuthCallback{
        void onExist();
        void onNotExist();
    }

    public void checkAuth(){
        mAuth=FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                firebaseAuthCallback.onExist();
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
                firebaseAuthCallback.onNotExist();
            }
        };
    }

    public void setOnFiresbaseAuthListener(FirebaseAuthCallback callback){
        firebaseAuthCallback=callback;
    }

    public void addAuthListener(){
        if(mAuth!=null){
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    public void removeAuthListener(){
        if (mAuth != null && mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}
