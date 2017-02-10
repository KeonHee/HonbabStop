package com.landvibe.android.honbabstop.Login.model;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.landvibe.android.honbabstop.base.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2017-02-09.
 */

public class LoginModel {

    private final static String TAG = "LoginModel";

    public static final String DOMAIN_KAKAO = "@kakao.com";
    public static final String PROVIDER_KAKAO = "kakao";

    public static final String PROVIDER_FACEBOOK = "facebook";
    public static final String PROVIDER_GOOGLE = "google";


    private Activity mActivity;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;


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
        setAuthListener();
    }
    public void setAuthListener(){
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                firebaseAuthCallback.onExist();
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
                firebaseAuthCallback.onNotExist();
            }
        };
    }
    public void addAuthListener(){
        if(mAuth!=null && mAuthListener!=null){
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
    public void removeAuthListener(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
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
                            user.setProfileUrl(firebaseUser.getPhotoUrl().toString());
                            user.setName(firebaseUser.getDisplayName());
                            user.setProviderId(PROVIDER_KAKAO);
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
     * Facebook OAuth 로그인
     * @param token
     * @param jsonObject
     */
    public void onFacebookFirebaseLogin(AccessToken token, JSONObject jsonObject) {
        if (mAuth!=null){
            Log.d(TAG, "onFacebookFirebaseLogin:" + token);
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, task -> {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        //{"id":"1202531989863702","name":"KeonHee  Kim","email":"kimgh6554@hanmail.net","gender":"male"}
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User user = new User();
                        user.setUid(firebaseUser.getUid());
                        user.setProviderId(PROVIDER_FACEBOOK);
                        try {
                            user.setEmail(jsonObject.getString("email"));
                            user.setName(jsonObject.getString("name"));
                            user.setGender(jsonObject.getString("gender"));

                            Uri profileUri = buildProfileUri(
                                    jsonObject.getString("id"),
                                    "large"
                            );
                            user.setProfileUrl(profileUri.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        writeUser(user);
                        firebaseLoginCallback.onSuccess();

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            firebaseLoginCallback.onFailure();
                        }
                    });
        }
    }

    /**
     * Facebook 프로필 Uri 생성
     */
    private Uri buildProfileUri(String id, String type){
        //"graph.facebook.com/facebookid/picture?type=type_value";
        // type : large, normal, small, square
        return new Uri.Builder()
                .scheme("http")
                .authority("graph.facebook.com")
                .appendPath(id)
                .appendPath("picture")
                .appendQueryParameter("type",type)
                .build();
    }
    /**
     * Firebase DB에 유저 정보 등록
     * @param user
     */
    private void writeUser(User user){
        Log.d(TAG,"writeUser()");
        mDatabase.child("users").child(user.getUid()).setValue(user);
    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if(mAuth!=null){
            Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, task -> {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User user = new User();
                        user.setUid(firebaseUser.getUid());
                        user.setProviderId(PROVIDER_GOOGLE);
                        user.setEmail(acct.getEmail());
                        user.setName(acct.getDisplayName());
                        //user.setGender();
                        user.setProfileUrl(acct.getPhotoUrl().toString());

                        writeUser(user);
                        firebaseLoginCallback.onSuccess();

                        if (!task.isSuccessful()) {
                            firebaseLoginCallback.onFailure();
                        }
                    });
        }
    }

}
