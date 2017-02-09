package com.landvibe.android.honbabstop.Login.model;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.landvibe.android.honbabstop.base.domain.User;

/**
 * Created by user on 2017-02-09.
 */

public class LoginModel {

    private final static String TAG = "LoginModel";

    public static final String KAKAODOMAIN = "@kakao.com";
    public static final String KAKAOPROVIDER = "kakao";

    private Activity mActivity;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseLoginCallback firebaseLoginCallback;
    private FirebaseSignUpCallback firebaseSignUpCallback;

    public interface FirebaseLoginCallback{
        void onSuccess();
        void onSignUp(String email, String password);
        void onFailure();
    }

    public interface FirebaseSignUpCallback{
        void onSuccess(String email, String password);
        void onFailure();
    }

    public void setActivity(Activity activity) {
        mActivity=activity;
    }

    public void setOnLoginLisnter(FirebaseLoginCallback callback){
        firebaseLoginCallback=callback;
    }
    public void setOnSignupLisnter(FirebaseSignUpCallback callback){
        firebaseSignUpCallback=callback;
    }

    public void setFirebaseAuth(FirebaseAuth auth){
        mAuth=auth;
    }

    public void setFirebaseReference(DatabaseReference reference){
        mDatabase=reference;
    }

    public void firebaseLogin(String email, String password){
        if(mAuth!=null){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mActivity, task -> {
                        if(task.isSuccessful()){
                            Log.d(TAG,"Firebase login success");
                            firebaseLoginCallback.onSuccess();
                        }else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException){
                                firebaseLoginCallback.onSignUp(email,password);
                            }
                            Log.d(TAG,"Firebase login fail: " + task.getException());
                            firebaseLoginCallback.onFailure();
                        }

                    });
        }
    }

    public void firebaseSignup(String email, String password){
        if(mAuth!=null){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mActivity, task -> {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Firebase sign up success");

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            User user = new User();
                            user.setUid(firebaseUser.getUid());
                            user.setEmail(firebaseUser.getEmail());
                            user.setProfileUrl(firebaseUser.getPhotoUrl());
                            user.setName(firebaseUser.getDisplayName());

                            if(firebaseUser.getProviderId().equals("firebase")){
                                user.setProviderId(KAKAOPROVIDER);
                            }else {
                                user.setProviderId(firebaseUser.getProviderId());
                            }
                            writeUser(user); // DB 저장

                            firebaseSignUpCallback.onSuccess(email,password);
                        }else {
                            Log.d(TAG,"Firebase sign up fail : " + task.getException());
                            firebaseSignUpCallback.onFailure();
                        }
                    });
        }
    }

    private void writeUser(User user){
        Log.d(TAG,"writeUser()");
        mDatabase.child("users").child(user.getUid()).setValue(user);
    }
}
