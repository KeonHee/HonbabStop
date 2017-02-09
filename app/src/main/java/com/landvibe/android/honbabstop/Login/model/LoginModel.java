package com.landvibe.android.honbabstop.Login.model;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private FirebaseAuthCallback firebaseAuthCallback;

    public interface FirebaseLoginCallback{
        void onSuccess();
        void onSignUp(String email, String password);
        void onFailure();
    }

    public interface FirebaseSignUpCallback{
        void onSuccess(String email, String password);
        void onFailure();
    }

    public interface FirebaseAuthCallback{
        void onExist();
        void onNotExist();
    }

    public void setActivity(Activity activity) {
        mActivity=activity;
    }

    /**
     * 인증 정보 로드
     */
    public void loadAuth(){
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            firebaseAuthCallback.onExist();
        }else {
            firebaseAuthCallback.onNotExist();
        }
    }

    /**
     * DB instance 로드
     */
    public void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * 콜백 등록
     */
    public void setOnLoginLisenter(FirebaseLoginCallback callback){
        firebaseLoginCallback=callback;
    }
    public void setOnSignupLisenter(FirebaseSignUpCallback callback){
        firebaseSignUpCallback=callback;
    }
    public void setOnAuthLisenter(FirebaseAuthCallback callback){
        firebaseAuthCallback = callback;
    }

    /**
     * Firebase 로그인
     * @param email
     * @param password
     */
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

    /**
     * Firebase 회원 가입
     * @param email
     * @param password
     */
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

    /**
     * Firebase DB에 유저 정보 등록
     * @param user
     */
    private void writeUser(User user){
        Log.d(TAG,"writeUser()");
        mDatabase.child("users").child(user.getUid()).setValue(user);
    }
}
