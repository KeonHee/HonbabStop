package com.landvibe.android.honbabstop.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.landvibe.android.honbabstop.Main.MainActivity;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.User;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";

    public static final String KAKAODOMAIN = "@kakao.com";
    public static final String KAKAOPROVIDER = "kakao";

    private SessionCallback callback;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        /* firebase auth */
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            // 로그인 상태일때 MainActivity로 이동
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        mDatabase= FirebaseDatabase.getInstance().getReference();

        /* kakao login */
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();


        /* change status bar*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.deep_orange));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d(TAG, "handleActivityResult()");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }


    /**
     * 카카오톡 로그인 결과 수신 콜백
     * */
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d(TAG, "onSessionOpened()");
            showLoading();
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d(TAG, "onSessionOpenFailed()");
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    /**
     * 카카오톡 프로필 요청
     */
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                redirectLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                long userId = userProfile.getId();
                String nickName = userProfile.getNickname();
                Log.d(TAG, "userId = " + userId);
                Log.d(TAG, "nickName= " + nickName);

                // firebase 로그인 또는 회원가입
                createIdAndLoginOnFirebase(userId);
            }

            @Override
            public void onNotSignedUp() {
                // 로그인 실패
            }
        });
    }

    /**
     * Firebase 로그인 or 회원가입
     *
     * @param userId
     */
    public void createIdAndLoginOnFirebase(long userId){

        StringBuffer emailBuffer = new StringBuffer();
        emailBuffer.append(String.valueOf(userId));
        emailBuffer.append(KAKAODOMAIN);
        String email = emailBuffer.toString();

        String password = String.valueOf(userId);

        logIn(email, password);
    }

    /**
     * Firebase 로그인
     *
     * 로그인 정보가 없을 때, 회원가입
     */
    private void logIn(String email, String password){
        if(mAuth!=null){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if(task.isSuccessful()){
                            Log.d(TAG,"Firebase login success");
                            redirectMainActivity();
                        }else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException){
                                signUp(email,password);
                            }
                            Log.d(TAG,"Firebase login fail: " + task.getException());
                        }

                    });
        }
    }

    /**
     * Firebase 회원가입
     *
     * 회원 가입 후 로그인
     */
    private void signUp(String email, String password){
        if(mAuth!=null){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
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

                            logIn(email,password); //로그인
                        }else {
                            Log.d(TAG,"Firebase sign up fail : " + task.getException());
                        }
                    });
        }
    }
    private void writeUser(User user){
        Log.d(TAG,"writeUser()");
        mDatabase.child("users").child(user.getUid()).setValue(user);
    }

    private void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void finishLoading(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void redirectMainActivity(){
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
