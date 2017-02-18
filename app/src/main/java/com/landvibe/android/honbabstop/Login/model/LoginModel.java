package com.landvibe.android.honbabstop.Login.model;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.utils.GenderConvertUtils;
import com.landvibe.android.honbabstop.base.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2017-02-09.
 */

public class LoginModel {

    private final static String TAG = "LoginModel";

    public static final String PROVIDER_KAKAO = User.KAKAO;
    public static final String PROVIDER_FACEBOOK = User.FACEBOOK;
    public static final String PROVIDER_GOOGLE = User.GOOGLE;

    private Activity mActivity;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseLoginCallback firebaseLoginCallback;
    private FirebaseSignUpCallback firebaseSignUpCallback;
    private FirebaseAuthCallback firebaseAuthCallback;

    public interface FirebaseLoginCallback{
        void onSuccess();
        void onSignUp(String email, String password, String name, String profileUrl);
        void onFailure();
    }

    public interface FirebaseSignUpCallback{
        void onSuccess(String email, String password, String name, String profileUrl);
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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

            SharedPreferenceUtils.setBooleanPreference(
                    mActivity,SharedPreferenceUtils.SESSION_BOOLEAN_KEY, true);

            firebaseAuthCallback.onExist();
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
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
     * Firebase 로그인 (For Kakao)
     * @param email
     * @param password
     */
    public void firebaseLogin(String email, String password, String name, String profileUrl){
        if(mAuth!=null){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mActivity, task -> {
                        if(task.isSuccessful()){
                            Log.d(TAG,"Firebase login success");

                            SharedPreferenceUtils.setStringPreference(
                                    mActivity,SharedPreferenceUtils.PROVIDER_STRING_KEY, User.KAKAO);
                            SharedPreferenceUtils.setBooleanPreference(
                                    mActivity,SharedPreferenceUtils.SESSION_BOOLEAN_KEY, true);

                            firebaseLoginCallback.onSuccess();
                        }else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException){
                                firebaseLoginCallback.onSignUp(email,password, name, profileUrl);
                            }
                            Log.d(TAG,"Firebase login fail: " + task.getException());
                            firebaseLoginCallback.onFailure();
                        }

                    });
        }
    }

    /**
     * Firebase 회원 가입 (For Kakao)
     * @param email
     * @param password
     */
    public void firebaseSignup(String email, String password, String name, String profileUrl){
        if(mAuth!=null){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mActivity, task -> {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "Firebase sign up fail : " + task.getException());
                            firebaseSignUpCallback.onFailure();
                        }

                        Log.d(TAG, "Firebase sign up success");

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User user = new User();
                        user.setUid(firebaseUser.getUid());
                        user.setEmail(firebaseUser.getEmail());
                        user.setProfileUrl(profileUrl);
                        user.setName(name);
                        user.setProviderId(PROVIDER_KAKAO);
                        writeUser(user); // DB 저장

                        firebaseSignUpCallback.onSuccess(email,password,name,profileUrl);
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
            Log.d(TAG, "c:" + token);
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            firebaseLoginCallback.onFailure();
                            return;
                        }

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        //{"id":"1202531989863702","name":"KeonHee  Kim","email":"kimgh6554@hanmail.net","gender":"male"}
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        mDatabase.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "check user exist");
                                User preUser = dataSnapshot.getValue(User.class);
                                if(preUser==null) {
                                    User user = new User();
                                    user.setUid(firebaseUser.getUid());
                                    user.setProviderId(PROVIDER_FACEBOOK);
                                    try {
                                        user.setEmail(jsonObject.getString("email"));
                                        user.setName(jsonObject.getString("name"));
                                        user.setGender(GenderConvertUtils.convertGender(jsonObject.getString("gender")));
                                        user.setProfileUrl(
                                                buildProfileUri(jsonObject.getString("id")).toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    writeUser(user);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d(TAG, "onCancelled: " + databaseError);
                            }
                        });


                        SharedPreferenceUtils.setStringPreference(
                                mActivity,SharedPreferenceUtils.PROVIDER_STRING_KEY, User.FACEBOOK);
                        SharedPreferenceUtils.setBooleanPreference(
                                mActivity,SharedPreferenceUtils.SESSION_BOOLEAN_KEY, true);

                        firebaseLoginCallback.onSuccess();
                    });
        }
    }

    /**
     * Facebook 프로필 Uri 생성
     */
    private Uri buildProfileUri(String id){
        //"graph.facebook.com/facebookid/picture?width=500&height=500";
        // type : large, normal, small, square
        return new Uri.Builder()
                .scheme("http")
                .authority("graph.facebook.com")
                .appendPath(id)
                .appendPath("picture")
                .appendQueryParameter("width","500")
                .appendQueryParameter("height","500")
                .build();
    }

    /**
     * Google OAuth 로그인
     * @param acct
     */
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if(mAuth!=null){
            Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, task -> {
                        if (!task.isSuccessful()) {
                            firebaseLoginCallback.onFailure();
                            return;
                        }

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        mDatabase.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "check user exist");
                                User preUser = dataSnapshot.getValue(User.class);
                                if(preUser==null) {
                                    Log.d(TAG, "User Data Save DB");
                                    User user = new User();
                                    user.setUid(firebaseUser.getUid());
                                    user.setProviderId(PROVIDER_GOOGLE);
                                    user.setEmail(acct.getEmail());
                                    user.setName(acct.getDisplayName());
                                    //user.setGender();
                                    user.setProfileUrl(acct.getPhotoUrl().toString());
                                    writeUser(user);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d(TAG, "onCancelled: " + databaseError);
                            }
                        });

                        SharedPreferenceUtils.setStringPreference(
                                mActivity,SharedPreferenceUtils.PROVIDER_STRING_KEY, User.GOOGLE);

                        firebaseLoginCallback.onSuccess();
                    });
        }
    }

    /**
     * Firebase DB에 유저 정보 등록
     * @param user
     */
    private void writeUser(User user){
        if(mDatabase==null) {
            return;
        }
        Log.d(TAG,"writeUser()");
        mDatabase.child("users").child(user.getUid()).setValue(user);
        UserStore.getInstance().saveUser(user);

        Log.d(TAG,"UserStore.getInstance().getUser()"+UserStore.getInstance().getUser());

        GlobalApp.getGlobalApplicationContext().changeModel(user);
    }

}
